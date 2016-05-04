import org.objectweb.asm.*;
import util.Cache;
import util.FieldRef;
import util.MethodRef;
import util.RefType;
import util.effect.EffectsCollector;
import util.effect.EffectsView;
import util.effect.FieldAssignEffect;
import util.effect.MethodCallEffect;
import util.except.UnknownConstSort;
import util.except.UnknownConstType;
import util.except.UnsupportedOpcodeException;
import util.frame.Frame;
import util.frame.MutableFrame;
import util.noop.NoOpInst;
import util.ref.Arbitrary;
import util.ref.Ref;
import util.ref.ReturnValRef;
import util.ref.UnaryOpRef;

import java.util.*;

public class MethodScanner extends MethodVisitor {
    private final MethodRef method;
    private final Cache cache;
    private final Frame frame;
    private final EffectsCollector effects = new EffectsCollector();

    private final Map<Label, Frame> visitedLabels = new HashMap<>();
    private final Map<Label, Frame> expectedLabels = new HashMap<>();

    private final NoOpInst noOpInst;

    public MethodScanner(MethodRef method, List<Ref> params, Cache cache) {
        super(Opcodes.ASM4);
        this.method = method;
        this.cache = cache;
        this.frame = new MutableFrame(method, params);
        this.noOpInst = new NoOpInst(cache, frame, effects);
    }

    @Override
    public void visitAttribute(Attribute attr) {
        super.visitAttribute(attr);
    }

    @Override
    public void visitCode() {
        super.visitCode();
    }

    @Override
    public void visitInsn(int opcode) {
        noOpInst.apply(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        switch (opcode) {
            case Opcodes.BIPUSH:
                frame.pushStack(cache.constOf((byte) operand));
                break;
            case Opcodes.SIPUSH:
                frame.pushStack(cache.constOf((short) operand));
                break;
            case Opcodes.NEWARRAY:
                frame.replaceStack(1, Arbitrary.val(RefType.OBJECTREF));
                break;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        switch (opcode) {
            case Opcodes.ILOAD:
            case Opcodes.FLOAD:
            case Opcodes.LLOAD:
            case Opcodes.DLOAD:
            case Opcodes.ALOAD:
                frame.pushStack(frame.getLocal(var));
                break;
            case Opcodes.ISTORE:
            case Opcodes.FSTORE:
            case Opcodes.LSTORE:
            case Opcodes.DSTORE:
            case Opcodes.ASTORE:
                frame.setLocal(var, frame.popStack());
                break;
            case Opcodes.RET:
                // TODO: jmp
                break;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        switch (opcode) {
            case Opcodes.NEW:
                frame.pushStack(Arbitrary.val(RefType.OBJECTREF));
                break;
            case Opcodes.ANEWARRAY:
                frame.replaceStack(1, Arbitrary.val(RefType.OBJECTREF));
                break;
            case Opcodes.CHECKCAST:
                break;
            case Opcodes.INSTANCEOF:
                frame.pushStack(Arbitrary.val(RefType.INT));
                break;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        RefType type = RefType.fromAsmType(Type.getType(desc));
        FieldRef field = FieldRef.of(owner, name, type);

        switch (opcode) {
            case Opcodes.GETFIELD:
                frame.popStack();
            case Opcodes.GETSTATIC:
                frame.pushStack(field);
                break;
            case Opcodes.PUTFIELD:
                effects.addFieldAssign(FieldAssignEffect.of(field, frame.popStack()));
                frame.popStack();
                break;
            case Opcodes.PUTSTATIC:
                effects.addFieldAssign(FieldAssignEffect.of(field, frame.popStack()));
                break;
        }
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        // TODO
        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        MethodRef method = MethodRef.of(owner, name);
        ArrayList<Ref> params = new ArrayList<>();
        Type methodType = Type.getMethodType(desc);

        if (opcode != Opcodes.INVOKESTATIC)
            params.add(frame.popStack());
        for (Type type : methodType.getArgumentTypes()) {
            params.add(frame.popStack());
        }
        Collections.reverse(params);

        Type returnType = methodType.getReturnType();
        if (!Type.VOID_TYPE.equals(returnType)) {
            ReturnValRef ret = new ReturnValRef(method, params, RefType.fromAsmType(returnType));
            frame.pushStack(ret);
        }

        MethodCallEffect effect = new MethodCallEffect(method, params);
        effects.addMethodCall(effect);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        if (cst instanceof Integer) {
            frame.pushStack(cache.constOf((int) cst));
        } else if (cst instanceof Float) {
            frame.pushStack(cache.constOf((float) cst));
        } else if (cst instanceof Long) {
            frame.pushStack(cache.constOf((long) cst));
        } else if (cst instanceof Double) {
            frame.pushStack(cache.constOf((double) cst));
        } else if (cst instanceof String) {
            frame.pushStack(cache.constOf((String) cst));
        } else if (cst instanceof Type) {
            int sort = ((Type) cst).getSort();
            if (sort == Type.OBJECT) {
                frame.pushStack(Arbitrary.val(RefType.OBJECTREF));
            } else if (sort == Type.ARRAY) {
                frame.pushStack(Arbitrary.val(RefType.OBJECTREF));
            } else if (sort == Type.METHOD) {
                // TODO:
            } else {
                throw new UnknownConstSort(cst, sort);
            }
        } else if (cst instanceof Handle) {
            // TODO
        } else {
            throw new UnknownConstType(cst);
        }

    }

    @Override
    public void visitIincInsn(int index, int delta) {
        Ref oldVal = frame.getLocal(index);
        UnaryOpRef newVal = UnaryOpRef.of(a -> (int) a + delta, oldVal, RefType.INT);
        frame.setLocal(index, newVal);
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        frame.replaceStack(dims, Arbitrary.val(RefType.OBJECTREF));
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }

    @Override
    public void visitLabel(Label label) {
        visitedLabels.put(label, frame.copy());

        Frame expectedFrame = expectedLabels.remove(label);
        if (expectedFrame != null) {
            frame.merge(expectedFrame);
        }
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        boolean conditional;
        switch (opcode) {
            case Opcodes.IFEQ:
            case Opcodes.IFNE:
            case Opcodes.IFLT:
            case Opcodes.IFGE:
            case Opcodes.IFGT:
            case Opcodes.IFLE:
            case Opcodes.IFNULL:
            case Opcodes.IFNONNULL:
                frame.popStack();
                conditional = true;
                break;
            case Opcodes.IF_ICMPEQ:
            case Opcodes.IF_ICMPNE:
            case Opcodes.IF_ICMPLT:
            case Opcodes.IF_ICMPGE:
            case Opcodes.IF_ICMPGT:
            case Opcodes.IF_ICMPLE:
            case Opcodes.IF_ACMPEQ:
            case Opcodes.IF_ACMPNE:
                frame.replaceStack(2);
                conditional = true;
                break;
            case Opcodes.JSR:
                frame.pushStack(Arbitrary.val(RefType.OBJECTREF));
            case Opcodes.GOTO:
                conditional = false;
                break;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }

        Frame visitedFrame = visitedLabels.get(label);
        if (visitedFrame == null) {
            Frame expectedFrame = expectedLabels.get(label);
            if (expectedFrame != null) {
                expectedFrame.merge(this.frame);
            } else {
                expectedLabels.put(label, this.frame.copy());
            }
        } else {
            this.frame.invalidatingMerge(visitedFrame);
        }
        if (!conditional) {
            this.frame.setInVacuum();
        }

//        Frame otherBranchFrame = expectedLabels.get(label);
//        if (otherBranchFrame != null) {
//            frame.merge(otherBranchFrame);
//        }
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

    public EffectsView getEffects() {
        return effects.build();
    }

    public MethodRef getMethod() {
        return method;
    }
}

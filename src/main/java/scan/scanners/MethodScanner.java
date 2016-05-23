package scan.scanners;

import org.objectweb.asm.*;
import scan.Cache;
import scan.FieldRef;
import scan.MethodRef;
import scan.RefType;
import scan.effect.EffectsCollector;
import scan.effect.EffectsView;
import scan.effect.FieldAssignEffect;
import scan.effect.MethodCallEffect;
import scan.except.UnknownConstSort;
import scan.except.UnknownConstType;
import scan.except.UnsupportedOpcodeException;
import scan.frame.Frame;
import scan.frame.MutableFrame;
import scan.noop.NoOpInst;
import scan.ref.Arbitrary;
import scan.ref.Ref;
import scan.ref.ReturnValRef;
import scan.ref.op.BinOpRef;

import java.util.*;

public class MethodScanner extends MethodVisitor {
    private final MethodRef method;
    private final Cache cache;
    private final EffectsCollector effects;

    private final ContextScanner<MutableFrame> context;

    private final Map<Label, Frame> visitedLabels = new HashMap<>();
    private final Map<Label, Frame> expectedLabels = new HashMap<>();

    private final NoOpInst noOpInst;

    public MethodScanner(MethodRef method, List<Ref> params, Cache cache) {
        super(Opcodes.ASM4, new ContextScanner<>(new MutableFrame(method, params), MutableFrame::copy));
        //noinspection unchecked
        this.context = (ContextScanner<MutableFrame>) mv;
        this.method = method;
        this.cache = cache;
        this.effects = new EffectsCollector(method);
        this.noOpInst = new NoOpInst(cache, context::getCurFrame, effects);
    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
        noOpInst.apply(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        super.visitIntInsn(opcode, operand);
        Frame frame = context.getCurFrame();
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
        super.visitVarInsn(opcode, var);
        Frame frame = context.getCurFrame();
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
        super.visitTypeInsn(opcode, type);
        Frame frame = context.getCurFrame();
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
        super.visitFieldInsn(opcode, owner, name, desc);
        RefType type = RefType.fromAsmType(Type.getType(desc));
        FieldRef field = FieldRef.of(owner, name, type);

        Frame frame = context.getCurFrame();
        switch (opcode) {
            case Opcodes.GETFIELD:
                frame.popStack();
            case Opcodes.GETSTATIC:
                frame.pushStack(field);
                break;
            case Opcodes.PUTFIELD:
                effects.addFieldAssign(FieldAssignEffect.of(method, field, frame.popStack()));
                frame.popStack();
                break;
            case Opcodes.PUTSTATIC:
                effects.addFieldAssign(FieldAssignEffect.of(method, field, frame.popStack()));
                break;
        }
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
        // TODO
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        super.visitMethodInsn(opcode, owner, name, desc);
        MethodRef method = MethodRef.of(owner, name, desc);
        ArrayList<Ref> params = new ArrayList<>();
        Type methodType = Type.getMethodType(desc);

        Frame frame = context.getCurFrame();
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

        MethodCallEffect effect = new MethodCallEffect(method, method, params);
        effects.addMethodCall(effect);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        super.visitLdcInsn(cst);
        Frame frame = context.getCurFrame();
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
        super.visitIincInsn(index, delta);
        Frame frame = context.getCurFrame();
        Ref oldVal = frame.getLocal(index);
        Ref newVal = BinOpRef.of(Opcodes.IADD, oldVal, cache.constOf(delta));
        frame.setLocal(index, newVal);
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        super.visitMultiANewArrayInsn(desc, dims);
        Frame frame = context.getCurFrame();
        frame.replaceStack(dims, Arbitrary.val(RefType.OBJECTREF));
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }

    @Override
    public void visitLabel(Label label) {
        super.visitLabel(label);
        Frame frame = context.getCurFrame();
        visitedLabels.put(label, frame.copy());

        Frame expectedFrame = expectedLabels.remove(label);
        if (expectedFrame != null) {
            frame.merge(expectedFrame);
        }
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        super.visitJumpInsn(opcode, label);
        Frame frame = context.getCurFrame();
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
            addForwardBranch(label);
        } else {
            frame.invalidatingMerge(visitedFrame);
        }
        if (!conditional) {
            frame.setInVacuum();
        }

//        Frame otherBranchFrame = expectedLabels.get(label);
//        if (otherBranchFrame != null) {
//            frame.merge(otherBranchFrame);
//        }
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        super.visitTableSwitchInsn(min, max, dflt, labels);
        Frame frame = context.getCurFrame();
        frame.popStack();
        for (Label label : labels) {
            addForwardBranch(label);
        }
        addForwardBranch(dflt);
        frame.setInVacuum();
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        super.visitLookupSwitchInsn(dflt, keys, labels);
        Frame frame = context.getCurFrame();
        frame.popStack();
        for (Label label : labels) {
            addForwardBranch(label);
        }
        addForwardBranch(dflt);
        frame.setInVacuum();
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

    private void addForwardBranch(Label label) {
        Frame frame = context.getCurFrame();
        Frame expectedFrame = expectedLabels.get(label);
        if (expectedFrame != null) {
            expectedFrame.merge(frame);
        } else {
            expectedLabels.put(label, frame.copy());
        }
    }

    public EffectsView getEffects() {
        return effects.build();
    }

    public MethodRef getMethod() {
        return method;
    }
}

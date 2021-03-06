import jdk.internal.org.objectweb.asm.*;
import util.Cache;
import util.Frame;
import util.effect.EffectsCollector;
import util.except.OpcodeNotSupportedException;
import util.except.UnknownConstSort;
import util.except.UnknownConstType;
import util.ref.Any;
import util.ref.expr.IincExpr;

import java.util.ArrayList;

public class MethodScanner extends MethodVisitor {
    private final Cache cache = new Cache();
    private final Frame frame = new Frame("", new ArrayList<>());
    private final EffectsCollector effects = new EffectsCollector();

    private final NoOpInst noOpInst = new NoOpInst(cache, frame, effects);

    public MethodScanner() {
        super(Opcodes.ASM4);
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
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        super.visitFrame(type, nLocal, local, nStack, stack);
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
                frame.replaceStack(1, Any.val());
                break;
            default:
                throw new OpcodeNotSupportedException(opcode);
        }
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        switch (opcode) {
            case Opcodes.ILOAD:
            case Opcodes.FLOAD:
            case Opcodes.LLOAD:
            case Opcodes.DLOAD:
                frame.pushStack(frame.getLocal(var));
                break;
            case Opcodes.ALOAD:
                frame.pushStack(Any.val());
                break;
            case Opcodes.ISTORE:
            case Opcodes.FSTORE:
            case Opcodes.LSTORE:
            case Opcodes.DSTORE:
                frame.setLocal(var, frame.popStack());
                break;
            case Opcodes.ASTORE:
                frame.setLocal(var, Any.val());
                break;
            case Opcodes.RET:
                effects.addReturnValue(frame.popStack());
                break;
            default:
                throw new OpcodeNotSupportedException(opcode);
        }
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        switch (opcode) {
            case Opcodes.NEW:
                frame.pushStack(Any.val());
                break;
            case Opcodes.ANEWARRAY:
                frame.replaceStack(1, Any.val());
                break;
            case Opcodes.CHECKCAST:
                // objref -> objref
                break;
            case Opcodes.INSTANCEOF:
                frame.pushStack(Any.val());
                // Maybe just 0 and 1?
                break;
            default:
                throw new OpcodeNotSupportedException(opcode);
        }
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        super.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
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
                frame.pushStack(Any.val());
            } else if (sort == Type.ARRAY) {
                frame.pushStack(Any.val());
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
    public void visitIincInsn(int var, int increment) {
        IincExpr newVal = new IincExpr(frame.getLocal(var), increment);
        frame.setLocal(var, newVal);
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        frame.replaceStack(dims, Any.val());
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

}

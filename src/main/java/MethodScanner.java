import jdk.internal.org.objectweb.asm.*;
import util.Cache;
import util.Frame;
import util.effect.EffectsCollector;
import util.except.OpcodeNotSupportedException;
import util.except.UnknownConstType;
import util.ref.Any;
import util.ref.consts.*;
import util.ref.expr.IincExpr;

import java.util.ArrayList;

public class MethodScanner extends MethodVisitor {
    private final Cache cache = new Cache();
    private final Frame frame = new Frame("", new ArrayList<>());
    private final EffectsCollector effects = new EffectsCollector();

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
        NoOpInst.apply(frame, opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        switch (opcode) {
            case Opcodes.BIPUSH:
                frame.pushStack(new ByteConst((byte) operand));
                break;
            case Opcodes.SIPUSH:
                frame.pushStack(new ShortConst((short) operand));
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
            frame.pushStack(IntConst.of((int) cst));
        } else if (cst instanceof Float) {
            frame.pushStack( FloatConst.of((float) cst));
        } else if (cst instanceof Long) {
            frame.pushStack(LongConst.of((long) cst));
        } else if (cst instanceof Double) {
            frame.pushStack(DoubleConst.of((double) cst));
        } else if (cst instanceof String) {
            frame.pushStack(StringConst.of((String) cst));
        } else if (cst instanceof Type) {
            // TODO:
            int sort = ((Type) cst).getSort();
            if (sort == Type.OBJECT) {
                // ...
            } else if (sort == Type.ARRAY) {
                // ...
            } else if (sort == Type.METHOD) {
                // ...
            } else {
                // throw an exception
            }
        } else if (cst instanceof Handle) {
            // ...
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

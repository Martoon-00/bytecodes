package tree.value.op;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import scan.except.UnsupportedOpcodeException;
import tree.value.ConstValue;
import tree.value.MyValue;

public class NumBinOpValue extends BinOpValue {
    private final static int startOpcode = Opcodes.IADD;
    private final static Type[] types = {Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE};
    private final static String[] opSyms = {"+", "-", "*", "/", "%"};

    public NumBinOpValue(int opcode, MyValue a, MyValue b) {
        super(opcode, a, b);
    }

    @Override
    public Type getType() {
        return types[(opcode - startOpcode) % types.length];
    }

    @Override
    protected MyValue evaluate(ConstValue av, ConstValue bv) {
        Object a = av.getValue();
        Object b = bv.getValue();
        Object res;
        switch (opcode) {
            case Opcodes.IADD:
                res = (int) a + (int) b;
                break;
            case Opcodes.ISUB:
                res = (int) a - (int) b;
                break;
            case Opcodes.IMUL:
                res = (int) a * (int) b;
                break;
            case Opcodes.IDIV:
                res = (int) a / (int) b;
                break;
            case Opcodes.IREM:
                res = (int) a % (int) b;
                break;
            case Opcodes.LADD:
                res = (long) a + (long) b;
                break;
            case Opcodes.LSUB:
                res = (long) a - (long) b;
                break;
            case Opcodes.LMUL:
                res = (long) a * (long) b;
                break;
            case Opcodes.LDIV:
                res = (long) a / (long) b;
                break;
            case Opcodes.LREM:
                res = (long) a % (long) b;
                break;
            case Opcodes.FADD:
                res = (float) a + (float) b;
                break;
            case Opcodes.FSUB:
                res = (float) a - (float) b;
                break;
            case Opcodes.FMUL:
                res = (float) a * (float) b;
                break;
            case Opcodes.FDIV:
                res = (float) a / (float) b;
                break;
            case Opcodes.FREM:
                res = (float) a % (float) b;
                break;
            case Opcodes.DADD:
                res = (double) a + (double) b;
                break;
            case Opcodes.DSUB:
                res = (double) a - (double) b;
                break;
            case Opcodes.DMUL:
                res = (double) a * (double) b;
                break;
            case Opcodes.DDIV:
                res = (double) a / (double) b;
                break;
            case Opcodes.DREM:
                res = (double) a % (double) b;
                break;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
        return new ConstValue(getType(), res);
    }

    public String toString() {
        return showInfixOp(opSyms[(opcode - startOpcode) / types.length]);
    }
}

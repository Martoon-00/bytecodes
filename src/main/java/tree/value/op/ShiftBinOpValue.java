package tree.value.op;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import scan.except.UnsupportedOpcodeException;
import tree.value.ConstValue;
import tree.value.MyValue;

public class ShiftBinOpValue extends BinOpValue {
    private final static int startOpcode = Opcodes.ISHL;
    private final static Type[] types = {Type.INT_TYPE, Type.LONG_TYPE};
    private final static String[] opSyms = {"<<", ">>", ">>>"};


    public ShiftBinOpValue(int opcode, MyValue a, MyValue b) {
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
            case Opcodes.ISHL:
                res = (int) a << (int) b;
                break;
            case Opcodes.ISHR:
                res = (int) a >> (int) b;
                break;
            case Opcodes.IUSHR:
                res = (int) a >>> (int) b;
                break;
            case Opcodes.LSHL:
                res = (long) a << (int) b;
                break;
            case Opcodes.LSHR:
                res = (long) a >> (long) b;
                break;
            case Opcodes.LUSHR:
                res = (long) a >>> (long) b;
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

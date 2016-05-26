package tree.value.op;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import scan.except.UnsupportedOpcodeException;
import tree.value.ConstValue;
import tree.value.MyValue;

public class LogicBinOpValue extends BinOpValue {
    private final static int startOpcode = Opcodes.IAND;
    private final static Type[] types = {Type.INT_TYPE, Type.LONG_TYPE};
    private final static String[] opSyms = {"&", "|", "^"};


    public LogicBinOpValue(int opcode, MyValue a, MyValue b) {
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
            case Opcodes.IAND:
                res = (int) a & (int) b;
                break;
            case Opcodes.IOR:
                res = (int) a | (int) b;
                break;
            case Opcodes.IXOR:
                res = (int) a ^ (int) b;
                break;
            case Opcodes.LAND:
                res = (long) a & (long) b;
                break;
            case Opcodes.LOR:
                res = (long) a | (long) b;
                break;
            case Opcodes.LXOR:
                res = (long) a ^ (long) b;
                break;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
        return new ConstValue(getType(), res);
    }

    public String toString() {
        return showInfixOp(opSyms[(opcode - startOpcode) / opSyms.length]);
    }
}



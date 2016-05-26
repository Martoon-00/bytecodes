package tree.value.op;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import scan.except.UnsupportedOpcodeException;
import tree.value.ConstValue;
import tree.value.MyValue;

public class NegUnaryOpValue extends UnaryOpValue {
    private final static int startOpcode = Opcodes.INEG;
    private final static Type[] types = {Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE};

    public NegUnaryOpValue(int opcode, MyValue a) {
        super(opcode, a);
    }

    @Override
    public Type getType() {
        return types[opcode - startOpcode];
    }

    @Override
    protected MyValue evaluate(ConstValue av) {
        Object a = av.getValue();
        Object res;
        switch (opcode) {
            case Opcodes.INEG:
                res = -(int) a;
                break;
            case Opcodes.LNEG:
                res = -(long) a;
                break;
            case Opcodes.FNEG:
                res = -(float) a;
                break;
            case Opcodes.DNEG:
                res = -(double) a;
                break;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
        return new ConstValue(getType(), res);
    }

    public String toString() {
        return "(-" + a.toString() + ")";
    }
}

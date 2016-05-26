package tree.value.op;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import scan.except.UnsupportedOpcodeException;
import tree.value.ConstValue;
import tree.value.MyValue;

public class CmpBinOpValue extends BinOpValue {
    public CmpBinOpValue(int opcode, MyValue a, MyValue b) {
        super(opcode, a, b);
    }

    @Override
    public Type getType() {
        return Type.INT_TYPE;
    }

    @Override
    protected MyValue evaluate(ConstValue av, ConstValue bv) {
        Object a = av.getValue();
        Object b = bv.getValue();
        Object res;
        switch (opcode) {
            case Opcodes.LCMP:
                res = Long.compare(((long) a), ((long) b));
                break;
            case Opcodes.FCMPL:
                res = cmp(((float) a), ((float) b), -1F);
                break;
            case Opcodes.FCMPG:
                res = cmp(((float) a), ((float) b), 1F);
                break;
            case Opcodes.DCMPL:
                res = cmp(((double) a), ((double) b), -1.0);
                break;
            case Opcodes.DCMPG:
                res = cmp(((double) a), ((double) b), 1.0);
                break;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
        return new ConstValue(getType(), res);
    }

    private float cmp(float a, float b, float ifNaN) {
        if (Float.isNaN(a) || Float.isNaN(b))
            return ifNaN;
        return Float.compare(a, b);
    }

    private double cmp(double a, double b, double ifNaN) {
        if (Double.isNaN(a) || Double.isNaN(b))
            return ifNaN;
        return Double.compare(a, b);
    }

    public String toString() {
        String opName;
        switch (opcode) {
            case Opcodes.LCMP:
                opName = "cmp";
                break;
            case Opcodes.FCMPL:
            case Opcodes.DCMPL:
                opName = "cmpl";
                break;
            case Opcodes.FCMPG:
            case Opcodes.DCMPG:
                opName = "cmpg";
                break;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
        return showPrefixOp(opName);
    }
}

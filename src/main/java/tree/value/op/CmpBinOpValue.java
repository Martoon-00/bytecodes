package tree.value.op;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import scan.except.UnsupportedOpcodeException;
import tree.value.MyValue;

public class CmpBinOpValue extends BinOpValue {
    public CmpBinOpValue(int opcode, MyValue a, MyValue b) {
        super(opcode, a, b);
    }

    @Override
    protected Type evalType() {
        switch (opcode) {
            case Opcodes.LCMP:
                return Type.LONG_TYPE;
            case Opcodes.FCMPL:
            case Opcodes.FCMPG:
                return Type.FLOAT_TYPE;
            case Opcodes.DCMPL:
            case Opcodes.DCMPG:
                return Type.DOUBLE_TYPE;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
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

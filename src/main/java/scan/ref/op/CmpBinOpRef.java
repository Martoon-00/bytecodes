package scan.ref.op;

import org.objectweb.asm.Opcodes;
import scan.RefType;
import scan.except.UnsupportedOpcodeException;
import scan.ref.Ref;

public class CmpBinOpRef extends BinOpRef {
    public CmpBinOpRef(int opcode, Ref a, Ref b) {
        super(opcode, a, b);
    }

    @Override
    protected RefType evalType() {
        switch (opcode) {
            case Opcodes.LCMP:
                return RefType.LONG;
            case Opcodes.FCMPL:
            case Opcodes.FCMPG:
                return RefType.FLOAT;
            case Opcodes.DCMPL:
            case Opcodes.DCMPG:
                return RefType.DOUBLE;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
    }

    @Override
    protected String show() {
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

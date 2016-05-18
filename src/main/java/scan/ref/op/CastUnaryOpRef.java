package scan.ref.op;

import org.objectweb.asm.Opcodes;
import scan.RefType;
import scan.except.UnsupportedOpcodeException;
import scan.ref.Ref;

public class CastUnaryOpRef extends UnaryOpRef {

    public CastUnaryOpRef(int opcode, Ref a) {
        super(opcode, a);
    }

    @Override
    protected RefType evalType() {
        switch (opcode) {
            case Opcodes.L2I:
            case Opcodes.F2I:
            case Opcodes.D2I:
            case Opcodes.I2B:
            case Opcodes.I2C:
            case Opcodes.I2S:
                return RefType.INT;
            case Opcodes.I2L:
            case Opcodes.F2L:
            case Opcodes.D2L:
                return RefType.LONG;
            case Opcodes.I2F:
            case Opcodes.L2F:
            case Opcodes.D2F:
                return RefType.FLOAT;
            case Opcodes.I2D:
            case Opcodes.L2D:
            case Opcodes.F2D:
                return RefType.DOUBLE;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
    }

    @Override
    protected String show() {
        return String.format("(%s) %s", evalType().toString().toLowerCase(), a.toString());
    }
}

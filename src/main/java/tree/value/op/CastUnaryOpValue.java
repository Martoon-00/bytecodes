package tree.value.op;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import scan.except.UnsupportedOpcodeException;
import tree.value.MyValue;

public class CastUnaryOpValue extends UnaryOpValue {

    public CastUnaryOpValue(int opcode, MyValue a) {
        super(opcode, a);
    }

    @Override
    public Type getType() {
        switch (opcode) {
            case Opcodes.L2I:
            case Opcodes.F2I:
            case Opcodes.D2I:
            case Opcodes.I2B:
            case Opcodes.I2C:
            case Opcodes.I2S:
                return Type.INT_TYPE;
            case Opcodes.I2L:
            case Opcodes.F2L:
            case Opcodes.D2L:
                return Type.LONG_TYPE;
            case Opcodes.I2F:
            case Opcodes.L2F:
            case Opcodes.D2F:
                return Type.FLOAT_TYPE;
            case Opcodes.I2D:
            case Opcodes.L2D:
            case Opcodes.F2D:
                return Type.DOUBLE_TYPE;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
    }

    public String toString() {
        return String.format("(%s) %s", getType().toString().toLowerCase(), a.toString());
    }
}

package util.noop;

import org.objectweb.asm.Opcodes;
import util.Cache;
import util.Frame;
import util.RefType;
import util.except.UnsupportedOpcodeException;
import util.op.UnaryOp;
import util.ref.Ref;
import util.ref.UnaryOpRef;

public class CastInst {
    private final Cache cache;
    private final Frame frame;

    public CastInst(Cache cache, Frame frame) {
        this.cache = cache;
        this.frame = frame;
    }

    public void apply(int opcode) {
        switch (opcode) {
            case Opcodes.L2I:
            case Opcodes.F2I:
            case Opcodes.D2I:
                applyUnaryOp(a -> cache.get((int) a), RefType.INT);
                break;
            case Opcodes.I2L:
            case Opcodes.F2L:
            case Opcodes.D2L:
                applyUnaryOp(a -> cache.get((long) a), RefType.LONG);
                break;
            case Opcodes.I2F:
            case Opcodes.L2F:
            case Opcodes.D2F:
                applyUnaryOp(a -> cache.get((float) a), RefType.FLOAT);
                break;
            case Opcodes.I2D:
            case Opcodes.L2D:
            case Opcodes.F2D:
                applyUnaryOp(a -> cache.get((double) a), RefType.DOUBLE);
                break;
            case Opcodes.I2B:
                applyUnaryOp(a -> cache.get((byte) a), RefType.BYTE);
                break;
            case Opcodes.I2C:
                applyUnaryOp(a -> cache.get((int)(char) a), RefType.CHAR);
                break;
            case Opcodes.I2S:
                applyUnaryOp(a -> cache.get((short) a), RefType.SHORT);
                break;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
    }

    private void applyUnaryOp(UnaryOp op, RefType type) {
        Ref a = frame.popStack();
        UnaryOpRef ref = UnaryOpRef.of(op, a, type);
        frame.pushStack(ref);
    }
}

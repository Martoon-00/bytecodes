package scan.noop;


import scan.Cache;
import scan.frame.Frame;
import scan.ref.Ref;
import scan.ref.op.UnaryOpRef;

import java.util.function.Supplier;

class CastInst {
    private final Cache cache;
    private final Supplier<Frame> curFrame;

    public CastInst(Cache cache, Supplier<Frame> curFrame) {
        this.cache = cache;
        this.curFrame = curFrame;
    }

    public void apply(int opcode) {
        Frame frame = curFrame.get();
        Ref v = frame.popStack();
        UnaryOpRef res = UnaryOpRef.of(opcode, v);
        frame.pushStack(res);

        /*
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
                applyUnaryOp(a -> cache.get((int) (byte) a), RefType.INT);
                break;
            case Opcodes.I2C:
                applyUnaryOp(a -> cache.get((int) (char) a), RefType.INT);
                break;
            case Opcodes.I2S:
                applyUnaryOp(a -> cache.get((int) (short) a), RefType.INT);
                break;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
        */
    }
}

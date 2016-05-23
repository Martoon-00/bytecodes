package scan.noop;

import scan.Cache;
import scan.frame.Frame;
import scan.ref.Ref;
import scan.ref.op.BinOpRef;

import java.util.function.Supplier;

class CmpInst {
    private final Cache cache;
    private final Supplier<Frame> curFrame;

    public CmpInst(Cache cache, Supplier<Frame> curFrame) {
        this.cache = cache;
        this.curFrame = curFrame;
    }

    public void apply(int opcode) {
        Frame frame = curFrame.get();
        Ref v2 = frame.popStack();
        Ref v1 = frame.popStack();
        BinOpRef res = BinOpRef.of(opcode, v1, v2);
        frame.pushStack(res);

        /*
        switch (opcode) {
            case Opcodes.LCMP:
                applyBinOp((a, b) -> cache.get(Long.compare((long) a, (long) b)), RefType.INT);
                break;
            case Opcodes.FCMPL:
                // TODO: remove extra cast
                applyBinOp((a, b) -> Float.isNaN((float) a) || Float.isNaN((float) b) ? -1 :
                        cache.get(Float.compare((float) a, (float) b)), RefType.INT);
                break;
            case Opcodes.FCMPG:
                applyBinOp((a, b) -> Float.isNaN((float) a) || Float.isNaN((float) b) ? 1 :
                        cache.get(Float.compare((float) a, (float) b)), RefType.INT);
                break;
            case Opcodes.DCMPL:
                applyBinOp((a, b) -> Double.isNaN((double) a) || Double.isNaN((double) b) ? -1 :
                        cache.get(Double.compare((double) a, (double) b)), RefType.INT);
                break;
            case Opcodes.DCMPG:
                applyBinOp((a, b) -> Double.isNaN((double) a) || Double.isNaN((double) b) ? 1 :
                        cache.get(Double.compare((double) a, (double) b)), RefType.INT);
                break;
            default:
                throw new UnsupportedOpcodeException(opcode);
        }*/

    }

}

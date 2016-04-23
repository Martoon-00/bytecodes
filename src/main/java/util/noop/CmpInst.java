package util.noop;

import org.objectweb.asm.Opcodes;
import util.Cache;
import util.Frame;
import util.RefType;
import util.except.UnsupportedOpcodeException;
import util.op.BinOp;
import util.ref.BinOpRef;
import util.ref.Ref;

public class CmpInst {
    private final Cache cache;
    private final Frame frame;

    public CmpInst(Cache cache, Frame frame) {
        this.cache = cache;
        this.frame = frame;
    }

    public void apply(int opcode) {
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
        }
    }

    private void applyBinOp(BinOp op, RefType type) {
        Ref a = frame.popStack();
        Ref b = frame.popStack();
        BinOpRef ref = BinOpRef.of(op, a, b, type);
        frame.pushStack(ref);
    }
}

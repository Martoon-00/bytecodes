package util.noop;


import org.objectweb.asm.Opcodes;
import util.Cache;
import util.RefType;
import util.frame.Frame;
import util.op.BinOp;
import util.op.UnaryOp;
import util.ref.BinOpRef;
import util.ref.Ref;
import util.ref.UnaryOpRef;

class NumOpInst {
    private final Cache cache;
    private final Frame frame;

    public NumOpInst(Cache cache, Frame frame) {
        this.cache = cache;
        this.frame = frame;
    }

    public void apply(int opcode) {
        switch (opcode) {
            case Opcodes.IADD:
                applyBinOp((a, b) -> cache.get((int) a + (int) b), RefType.INT);
                break;
            case Opcodes.LADD:
                applyBinOp((a, b) -> cache.get((long) a + (long) b), RefType.LONG);
                break;
            case Opcodes.FADD:
                applyBinOp((a, b) -> cache.get((float) a + (float) b), RefType.FLOAT);
                break;
            case Opcodes.DADD:
                applyBinOp((a, b) -> cache.get((double) a + (double) b), RefType.DOUBLE);
                break;
            case Opcodes.ISUB:
                applyBinOp((a, b) -> cache.get((int) a - (int) b), RefType.INT);
                break;
            case Opcodes.LSUB:
                applyBinOp((a, b) -> cache.get((long) a - (long) b), RefType.LONG);
                break;
            case Opcodes.FSUB:
                applyBinOp((a, b) -> cache.get((float) a - (float) b), RefType.FLOAT);
                break;
            case Opcodes.DSUB:
                applyBinOp((a, b) -> cache.get((double) a - (double) b), RefType.DOUBLE);
                break;
            case Opcodes.IMUL:
                applyBinOp((a, b) -> cache.get((int) a * (int) b), RefType.INT);
                break;
            case Opcodes.LMUL:
                applyBinOp((a, b) -> cache.get((long) a * (long) b), RefType.LONG);
                break;
            case Opcodes.FMUL:
                applyBinOp((a, b) -> cache.get((float) a * (float) b), RefType.FLOAT);
                break;
            case Opcodes.DMUL:
                applyBinOp((a, b) -> cache.get((double) a * (double) b), RefType.DOUBLE);
                break;
            case Opcodes.IDIV:
                applyBinOp((a, b) -> cache.get((int) a / (int) b), RefType.INT);
                break;
            case Opcodes.LDIV:
                applyBinOp((a, b) -> cache.get((long) a / (long) b), RefType.LONG);
                break;
            case Opcodes.FDIV:
                applyBinOp((a, b) -> cache.get((float) a / (float) b), RefType.FLOAT);
                break;
            case Opcodes.DDIV:
                applyBinOp((a, b) -> cache.get((double) a / (double) b), RefType.DOUBLE);
                break;
            case Opcodes.IREM:
                applyBinOp((a, b) -> cache.get((int) a % (int) b), RefType.INT);
                break;
            case Opcodes.LREM:
                applyBinOp((a, b) -> cache.get((long) a % (long) b), RefType.LONG);
                break;
            case Opcodes.FREM:
                applyBinOp((a, b) -> cache.get((float) a % (float) b), RefType.FLOAT);
                break;
            case Opcodes.DREM:
                applyBinOp((a, b) -> cache.get((double) a % (double) b), RefType.DOUBLE);
                break;

            case Opcodes.INEG:
                applyUnaryOp(a -> cache.get(-(int) a), RefType.INT);
                break;
            case Opcodes.LNEG:
                applyUnaryOp(a -> cache.get(-(long) a), RefType.LONG);
                break;
            case Opcodes.FNEG:
                applyUnaryOp(a -> cache.get(-(float) a) , RefType.FLOAT);
                break;
            case Opcodes.DNEG:
                applyUnaryOp(a -> cache.get(-(double) a) , RefType.DOUBLE);
                break;

            case Opcodes.ISHL:
                applyBinOp((a, b) -> cache.get((int) a << (int) b), RefType.INT);
                break;
            case Opcodes.LSHL:
                applyBinOp((a, b) -> cache.get((long) a << (int) b), RefType.LONG);
                break;
            case Opcodes.ISHR:
                applyBinOp((a, b) -> cache.get((int) a >> (int) b), RefType.INT);
                break;
            case Opcodes.LSHR:
                applyBinOp((a, b) -> cache.get((int) a >> (int) b), RefType.LONG);
                break;
            case Opcodes.IUSHR:
                applyBinOp((a, b) -> cache.get((int) a >>> (int) b), RefType.INT);
                break;
            case Opcodes.LUSHR:
                applyBinOp((a, b) -> cache.get((long) a >>> (int) b), RefType.LONG);
                break;

            case Opcodes.IAND:
                applyBinOp((a, b) -> cache.get((int) a & (int) b), RefType.INT);
                break;
            case Opcodes.LAND:
                applyBinOp((a, b) -> cache.get((long) a & (long) b), RefType.LONG);
                break;
            case Opcodes.IOR:
                applyBinOp((a, b) -> cache.get((int) a | (int) b), RefType.INT);
                break;
            case Opcodes.LOR:
                applyBinOp((a, b) -> cache.get((long) a | (long) b), RefType.LONG);
                break;
            case Opcodes.IXOR:
                applyBinOp((a, b) -> cache.get((int) a ^ (int) b), RefType.INT);
                break;
            case Opcodes.LXOR:
                applyBinOp((a, b) -> cache.get((long) a ^ (long) b), RefType.LONG);
                break;
        }
    }


    private void applyBinOp(BinOp op, RefType type) {
        Ref a = frame.popStack();
        Ref b = frame.popStack();
        BinOpRef ref = BinOpRef.of(op, a, b, type);
        frame.pushStack(ref);
    }

    private void applyUnaryOp(UnaryOp op, RefType type) {
        Ref a = frame.popStack();
        UnaryOpRef ref = UnaryOpRef.of(op, a, type);
        frame.pushStack(ref);
    }
}

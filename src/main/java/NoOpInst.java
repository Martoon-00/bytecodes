import org.objectweb.asm.Opcodes;
import util.Cache;
import util.Frame;
import util.effect.EffectsCollector;
import util.except.OpcodeNotSupportedException;
import util.ref.*;
import util.ref.consts.NullConst;

public class NoOpInst {
    private final Cache cache;
    private final Frame frame;
    private final EffectsCollector effects;

    public NoOpInst(Cache cache, Frame frame, EffectsCollector effects) {
        this.cache = cache;
        this.frame = frame;
        this.effects = effects;
    }

    public void apply(int opcode) {
        switch (opcode) {
            case Opcodes.NOP:
                break;

            // constants
            case Opcodes.ACONST_NULL:
                frame.pushStack(NullConst.val());
                break;
            case Opcodes.ICONST_M1:
                frame.pushStack(cache.constOf(-1));
                break;
            case Opcodes.ICONST_0:
                frame.pushStack(cache.constOf(0));
                break;
            case Opcodes.ICONST_1:
                frame.pushStack(cache.constOf(1));
                break;
            case Opcodes.ICONST_2:
                frame.pushStack(cache.constOf(2));
                break;
            case Opcodes.ICONST_3:
                frame.pushStack(cache.constOf(3));
                break;
            case Opcodes.ICONST_4:
                frame.pushStack(cache.constOf(4));
                break;
            case Opcodes.ICONST_5:
                frame.pushStack(cache.constOf(5));
                break;
            case Opcodes.LCONST_0:
                frame.pushStack(cache.constOf(0L));
                break;
            case Opcodes.LCONST_1:
                frame.pushStack(cache.constOf(1L));
                break;
            case Opcodes.FCONST_0:
                frame.pushStack(cache.constOf(0F));
                break;
            case Opcodes.FCONST_1:
                frame.pushStack(cache.constOf(1F));
                break;
            case Opcodes.FCONST_2:
                frame.pushStack(cache.constOf(2F));
                break;
            case Opcodes.DCONST_0:
                frame.pushStack(cache.constOf(0.));
                break;
            case Opcodes.DCONST_1:
                frame.pushStack(cache.constOf(1.));
                break;

            // store and load with arrays
            case Opcodes.IALOAD:
            case Opcodes.LALOAD:
            case Opcodes.FALOAD:
            case Opcodes.DALOAD:
            case Opcodes.AALOAD:
            case Opcodes.BALOAD:
            case Opcodes.CALOAD:
            case Opcodes.SALOAD:
                frame.replaceStack(2, Any.val());
                break;
            case Opcodes.IASTORE:
            case Opcodes.LASTORE:
            case Opcodes.FASTORE:
            case Opcodes.DASTORE:
            case Opcodes.AASTORE:
            case Opcodes.BASTORE:
            case Opcodes.CASTORE:
            case Opcodes.SASTORE:
                frame.replaceStack(3);
                break;

            // stack manipulation
            case Opcodes.POP:

                break;
            case Opcodes.POP2:

                break;
            case Opcodes.DUP:

                break;
            case Opcodes.DUP_X1:

                break;
            case Opcodes.DUP_X2:

                break;
            case Opcodes.DUP2:

                break;
            case Opcodes.DUP2_X1:

                break;
            case Opcodes.DUP2_X2:

                break;
            case Opcodes.SWAP:

                break;

            // number operations
            case Opcodes.IADD:
                applyBinOp((a, b) -> cache.get((int) a + (int) b));
                break;
            case Opcodes.LADD:
                applyBinOp((a, b) -> cache.get((long) a + (long) b));
                break;
            case Opcodes.FADD:
                applyBinOp((a, b) -> cache.get((float) a + (float) b));
                break;
            case Opcodes.DADD:
                applyBinOp((a, b) -> cache.get((double) a + (double) b));
                break;
            case Opcodes.ISUB:
                applyBinOp((a, b) -> cache.get((int) a - (int) b));
                break;
            case Opcodes.LSUB:
                applyBinOp((a, b) -> cache.get((long) a - (long) b));
                break;
            case Opcodes.FSUB:
                applyBinOp((a, b) -> cache.get((float) a - (float) b));
                break;
            case Opcodes.DSUB:
                applyBinOp((a, b) -> cache.get((double) a - (double) b));
                break;
            case Opcodes.IMUL:
                applyBinOp((a, b) -> cache.get((int) a * (int) b));
                break;
            case Opcodes.LMUL:
                applyBinOp((a, b) -> cache.get((long) a * (long) b));
                break;
            case Opcodes.FMUL:
                applyBinOp((a, b) -> cache.get((float) a * (float) b));
                break;
            case Opcodes.DMUL:
                applyBinOp((a, b) -> cache.get((double) a * (double) b));
                break;
            case Opcodes.IDIV:
                applyBinOp((a, b) -> cache.get((int) a / (int) b));
                break;
            case Opcodes.LDIV:
                applyBinOp((a, b) -> cache.get((long) a / (long) b));
                break;
            case Opcodes.FDIV:
                applyBinOp((a, b) -> cache.get((float) a / (float) b));
                break;
            case Opcodes.DDIV:
                applyBinOp((a, b) -> cache.get((double) a / (double) b));
                break;
            case Opcodes.IREM:
                applyBinOp((a, b) -> cache.get((int) a % (int) b));
                break;
            case Opcodes.LREM:
                applyBinOp((a, b) -> cache.get((long) a % (long) b));
                break;
            case Opcodes.FREM:
                applyBinOp((a, b) -> cache.get((float) a % (float) b));
                break;
            case Opcodes.DREM:
                applyBinOp((a, b) -> cache.get((double) a % (double) b));
                break;
            case Opcodes.INEG:
                applyUnaryOp(a -> cache.get((int) a));
                break;
            case Opcodes.LNEG:
                applyUnaryOp(a -> cache.get((long) a));
                break;
            case Opcodes.FNEG:
                applyUnaryOp(a -> cache.get((float) a));
                break;
            case Opcodes.DNEG:
                applyUnaryOp(a -> cache.get((double) a));
                break;
            case Opcodes.ISHL:
                applyBinOp((a, b) -> cache.get((int) a << (int) b));
                break;
            case Opcodes.LSHL:
                applyBinOp((a, b) -> cache.get((long) a << (int) b));
                break;
            case Opcodes.ISHR:
                applyBinOp((a, b) -> cache.get((int) a >> (int) b));
                break;
            case Opcodes.LSHR:
                applyBinOp((a, b) -> cache.get((int) a >> (int) b));
                break;
            case Opcodes.IUSHR:
                applyBinOp((a, b) -> cache.get((int) a >>> (int) b));
                break;
            case Opcodes.LUSHR:
                applyBinOp((a, b) -> cache.get((long) a >>> (int) b));
                break;
            case Opcodes.IAND:
                applyBinOp((a, b) -> cache.get((int) a & (int) b));
                break;
            case Opcodes.LAND:
                applyBinOp((a, b) -> cache.get((long) a & (long) b));
                break;
            case Opcodes.IOR:
                applyBinOp((a, b) -> cache.get((int) a | (int) b));
                break;
            case Opcodes.LOR:
                applyBinOp((a, b) -> cache.get((long) a | (long) b));
                break;
            case Opcodes.IXOR:
                applyBinOp((a, b) -> cache.get((int) a ^ (int) b));
                break;
            case Opcodes.LXOR:
                applyBinOp((a, b) -> cache.get((long) a ^ (long) b));
                break;

            // casts of primitive types
            case Opcodes.L2I:
            case Opcodes.F2I:
            case Opcodes.D2I:
                applyUnaryOp(a -> cache.get((int) a));
                break;
            case Opcodes.I2L:
            case Opcodes.F2L:
            case Opcodes.D2L:
                applyUnaryOp(a -> cache.get((long) a));
                break;
            case Opcodes.I2F:
            case Opcodes.L2F:
            case Opcodes.D2F:
                applyUnaryOp(a -> cache.get((float) a));
                break;
            case Opcodes.I2D:
            case Opcodes.L2D:
            case Opcodes.F2D:
                applyUnaryOp(a -> cache.get((double) a));
                break;
            case Opcodes.I2B:
                applyUnaryOp(a -> cache.get((byte) a));
                break;
            case Opcodes.I2C:
                applyUnaryOp(a -> cache.get((int)(char) a));
                break;
            case Opcodes.I2S:
                applyUnaryOp(a -> cache.get((short) a));
                break;

            // comparisons
            case Opcodes.LCMP:

                break;
            case Opcodes.FCMPL:

                break;
            case Opcodes.FCMPG:

                break;
            case Opcodes.DCMPL:

                break;
            case Opcodes.DCMPG:

                break;

            // return
            case Opcodes.IRETURN:
            case Opcodes.LRETURN:
            case Opcodes.FRETURN:
            case Opcodes.DRETURN:
            case Opcodes.ARETURN:
            case Opcodes.RETURN:
                effects.addReturnValue(frame.popStack());
                break;

            // array length
            case Opcodes.ARRAYLENGTH:
                // objref -> objref
                break;

            // throw
            case Opcodes.ATHROW:

                break;

            // monitors
            case Opcodes.MONITORENTER:
            case Opcodes.MONITOREXIT:
                frame.popStack();
                break;

            // unsupported
            default:
                throw new OpcodeNotSupportedException(opcode);
        }
    }

    private void applyBinOp(BinOp op) {
        Ref a = frame.popStack();
        Ref b = frame.popStack();
        BinOpRef ref = new BinOpRef(op, a, b);
        frame.pushStack(ref);
    }

    private void applyUnaryOp(UnaryOp op) {
        Ref a = frame.popStack();
        UnaryOpRef ref = new UnaryOpRef(op, a);
        frame.pushStack(ref);
    }
}

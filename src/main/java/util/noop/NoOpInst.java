package util.noop;

import org.objectweb.asm.Opcodes;
import util.Cache;
import util.Frame;
import util.RefType;
import util.effect.EffectsCollector;
import util.except.IllegalStackManipulationException;
import util.except.UnsupportedOpcodeException;
import util.op.BinOp;
import util.op.UnaryOp;
import util.ref.Arbitrary;
import util.ref.BinOpRef;
import util.ref.Ref;
import util.ref.UnaryOpRef;

public class NoOpInst {
    private final Cache cache;
    private final Frame frame;
    private final EffectsCollector effects;

    private final ConstInst constInst;
    private final ArrayInst arrayInst;

    public NoOpInst(Cache cache, Frame frame, EffectsCollector effects) {
        this.cache = cache;
        this.frame = frame;
        this.effects = effects;
        constInst = new ConstInst(cache, frame);
        arrayInst = new ArrayInst(this.frame);
    }

    public void apply(int opcode) {
        switch (opcode) {
            case Opcodes.NOP:
                break;

            // constants
            case Opcodes.ACONST_NULL:
            case Opcodes.ICONST_M1:
            case Opcodes.ICONST_0:
            case Opcodes.ICONST_1:
            case Opcodes.ICONST_2:
            case Opcodes.ICONST_3:
            case Opcodes.ICONST_4:
            case Opcodes.ICONST_5:
            case Opcodes.LCONST_0:
            case Opcodes.LCONST_1:
            case Opcodes.FCONST_0:
            case Opcodes.FCONST_1:
            case Opcodes.FCONST_2:
            case Opcodes.DCONST_0:
            case Opcodes.DCONST_1:
                constInst.apply(opcode);
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
            case Opcodes.IASTORE:
            case Opcodes.LASTORE:
            case Opcodes.FASTORE:
            case Opcodes.DASTORE:
            case Opcodes.AASTORE:
            case Opcodes.BASTORE:
            case Opcodes.CASTORE:
            case Opcodes.SASTORE:
                arrayInst.apply(opcode);
                break;

            // stack manipulation
            case Opcodes.POP:
                pop();
                break;
            case Opcodes.POP2:
                pop2();
                break;
            case Opcodes.DUP:
                dup();
                break;
            case Opcodes.DUP_X1:
                dup_x1();
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
                applyUnaryOp(a -> cache.get((int) a), RefType.INT);
                break;
            case Opcodes.LNEG:
                applyUnaryOp(a -> cache.get((long) a), RefType.LONG);
                break;
            case Opcodes.FNEG:
                applyUnaryOp(a -> cache.get((float) a) , RefType.FLOAT);
                break;
            case Opcodes.DNEG:
                applyUnaryOp(a -> cache.get((double) a) , RefType.DOUBLE);
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

            // casts of primitive types
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

            // comparisons
            case Opcodes.LCMP:
                applyBinOp((a, b) -> cache.get(Long.compare((long)a, (long)b)), RefType.INT);
                break;
            case Opcodes.FCMPL:
                // TODO: remove extra cast
                applyBinOp((a, b) -> Float.isNaN((float)a) || Float.isNaN((float)b) ? -1 :
                        cache.get(Float.compare((float)a, (float)b)), RefType.INT);
                break;
            case Opcodes.FCMPG:
                applyBinOp((a, b) -> Float.isNaN((float)a) || Float.isNaN((float)b) ? 1 :
                        cache.get(Float.compare((float)a, (float)b)), RefType.INT);
                break;
            case Opcodes.DCMPL:
                applyBinOp((a, b) -> Double.isNaN((double)a) || Double.isNaN((double)b) ? -1 :
                        cache.get(Double.compare((double)a, (double)b)), RefType.INT);
                break;
            case Opcodes.DCMPG:
                applyBinOp((a, b) -> Double.isNaN((double)a) || Double.isNaN((double)b) ? 1 :
                        cache.get(Double.compare((double)a, (double)b)), RefType.INT);
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
                frame.replaceStack(1, Arbitrary.val(RefType.INT));
                break;

            // throw
            case Opcodes.ATHROW:
                // TODO:
                break;

            // monitors
            case Opcodes.MONITORENTER:
            case Opcodes.MONITOREXIT:
                frame.popStack();
                break;

            // unsupported
            default:
                throw new UnsupportedOpcodeException(opcode);
        }
    }

    private void dup_x1() {
        Ref v1 = frame.popStack();
        Ref v2 = frame.popStack();
        if (v1.getType().hasDoubleSize() || v2.getType().hasDoubleSize())
            throw new IllegalStackManipulationException();
        frame.pushStack(v1);
        frame.pushStack(v2);
        frame.pushStack(v1);
    }

    private void dup() {
        Ref val = frame.popStack();
        if (val.getType().hasDoubleSize())
            throw new IllegalStackManipulationException();
        frame.pushStack(val);
        frame.pushStack(val);
    }

    private void pop() {
        Ref v = frame.popStack();
        if (v.getType().hasDoubleSize())
            throw new IllegalStackManipulationException();
    }

    private void pop2() {
        Ref v = frame.popStack();
        if (!v.getType().hasDoubleSize()) {
            pop();
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

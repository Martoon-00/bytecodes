package util.noop;

import org.objectweb.asm.Opcodes;
import util.Cache;
import util.Frame;
import util.RefType;
import util.effect.EffectsCollector;
import util.except.UnsupportedOpcodeException;
import util.ref.Arbitrary;

public class NoOpInst {
    private final Cache cache;
    private final Frame frame;
    private final EffectsCollector effects;

    private final ConstInst constInst;
    private final ArrayInst arrayInst;
    private final NumOpInst numOpInst;
    private final CastInst castInst;
    private final CmpInst cmpInst;
    private final StackModInst stackModInst;

    public NoOpInst(Cache cache, Frame frame, EffectsCollector effects) {
        this.cache = cache;
        this.frame = frame;
        this.effects = effects;

        constInst = new ConstInst(cache, frame);
        arrayInst = new ArrayInst(frame);
        numOpInst = new NumOpInst(cache, frame);
        castInst = new CastInst(cache, frame);
        cmpInst = new CmpInst(cache, frame);
        stackModInst = new StackModInst(frame);
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
            case Opcodes.POP2:
            case Opcodes.DUP:
            case Opcodes.DUP_X1:
            case Opcodes.DUP_X2:
            case Opcodes.DUP2:
            case Opcodes.DUP2_X1:
            case Opcodes.DUP2_X2:
            case Opcodes.SWAP:
                stackModInst.apply(opcode);
                break;

            // number operations
            case Opcodes.IADD:
            case Opcodes.LADD:
            case Opcodes.FADD:
            case Opcodes.DADD:
            case Opcodes.ISUB:
            case Opcodes.LSUB:
            case Opcodes.FSUB:
            case Opcodes.DSUB:
            case Opcodes.IMUL:
            case Opcodes.LMUL:
            case Opcodes.FMUL:
            case Opcodes.DMUL:
            case Opcodes.IDIV:
            case Opcodes.LDIV:
            case Opcodes.FDIV:
            case Opcodes.DDIV:
            case Opcodes.IREM:
            case Opcodes.LREM:
            case Opcodes.FREM:
            case Opcodes.DREM:
            case Opcodes.INEG:
            case Opcodes.LNEG:
            case Opcodes.FNEG:
            case Opcodes.DNEG:
            case Opcodes.ISHL:
            case Opcodes.LSHL:
            case Opcodes.ISHR:
            case Opcodes.LSHR:
            case Opcodes.IUSHR:
            case Opcodes.LUSHR:
            case Opcodes.IAND:
            case Opcodes.LAND:
            case Opcodes.IOR:
            case Opcodes.LOR:
            case Opcodes.IXOR:
            case Opcodes.LXOR:
                numOpInst.apply(opcode);
                break;

            // casts of primitive types
            case Opcodes.L2I:
            case Opcodes.F2I:
            case Opcodes.D2I:
            case Opcodes.I2L:
            case Opcodes.F2L:
            case Opcodes.D2L:
            case Opcodes.I2F:
            case Opcodes.L2F:
            case Opcodes.D2F:
            case Opcodes.I2D:
            case Opcodes.L2D:
            case Opcodes.F2D:
            case Opcodes.I2B:
            case Opcodes.I2C:
            case Opcodes.I2S:
                castInst.apply(opcode);
                break;

            // comparisons
            case Opcodes.LCMP:
            case Opcodes.FCMPL:
            case Opcodes.FCMPG:
            case Opcodes.DCMPL:
            case Opcodes.DCMPG:
                cmpInst.apply(opcode);
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

}

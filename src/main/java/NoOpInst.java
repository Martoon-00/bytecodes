import org.objectweb.asm.Opcodes;
import util.Frame;
import util.except.OpcodeNotSupportedException;
import util.ref.Any;
import util.ref.consts.*;

public class NoOpInst {
    public static void apply(Frame frame, int opcode) {

        switch (opcode) {
            case Opcodes.NOP:
                break;

            // constants
            case Opcodes.ACONST_NULL:
                frame.pushStack(NullConst.val());
                break;
            case Opcodes.ICONST_M1:
                frame.pushStack(IntConst.of(-1));
                break;
            case Opcodes.ICONST_0:
                frame.pushStack(IntConst.of(0));
                break;
            case Opcodes.ICONST_1:
                frame.pushStack(IntConst.of(1));
                break;
            case Opcodes.ICONST_2:
                frame.pushStack(IntConst.of(2));
                break;
            case Opcodes.ICONST_3:
                frame.pushStack(IntConst.of(3));
                break;
            case Opcodes.ICONST_4:
                frame.pushStack(IntConst.of(4));
                break;
            case Opcodes.ICONST_5:
                frame.pushStack(IntConst.of(5));
                break;
            case Opcodes.LCONST_0:
                frame.pushStack(LongConst.of(0L));
                break;
            case Opcodes.LCONST_1:
                frame.pushStack(LongConst.of(1L));
                break;
            case Opcodes.FCONST_0:
                frame.pushStack(FloatConst.of(0F));
                break;
            case Opcodes.FCONST_1:
                frame.pushStack(FloatConst.of(1F));
                break;
            case Opcodes.FCONST_2:
                frame.pushStack(FloatConst.of(2F));
                break;
            case Opcodes.DCONST_0:
                frame.pushStack(DoubleConst.of(0.));
                break;
            case Opcodes.DCONST_1:
                frame.pushStack(DoubleConst.of(1.));
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

                break;
            case Opcodes.LADD:

                break;
            case Opcodes.FADD:

                break;
            case Opcodes.DADD:

                break;
            case Opcodes.ISUB:

                break;
            case Opcodes.LSUB:

                break;
            case Opcodes.FSUB:

                break;
            case Opcodes.DSUB:

                break;
            case Opcodes.IMUL:

                break;
            case Opcodes.LMUL:

                break;
            case Opcodes.FMUL:

                break;
            case Opcodes.DMUL:

                break;
            case Opcodes.IDIV:

                break;
            case Opcodes.LDIV:

                break;
            case Opcodes.FDIV:

                break;
            case Opcodes.DDIV:

                break;
            case Opcodes.IREM:

                break;
            case Opcodes.LREM:

                break;
            case Opcodes.FREM:

                break;
            case Opcodes.DREM:

                break;
            case Opcodes.INEG:

                break;
            case Opcodes.LNEG:

                break;
            case Opcodes.FNEG:

                break;
            case Opcodes.DNEG:

                break;
            case Opcodes.ISHL:

                break;
            case Opcodes.LSHL:

                break;
            case Opcodes.ISHR:

                break;
            case Opcodes.LSHR:

                break;
            case Opcodes.IUSHR:

                break;
            case Opcodes.LUSHR:

                break;
            case Opcodes.IAND:

                break;
            case Opcodes.LAND:

                break;
            case Opcodes.IOR:

                break;
            case Opcodes.LOR:

                break;
            case Opcodes.IXOR:

                break;
            case Opcodes.LXOR:

                break;

            // casts of primitive types
            case Opcodes.I2L:

                break;
            case Opcodes.I2F:

                break;
            case Opcodes.I2D:

                break;
            case Opcodes.L2I:

                break;
            case Opcodes.L2F:

                break;
            case Opcodes.L2D:

                break;
            case Opcodes.F2I:

                break;
            case Opcodes.F2L:

                break;
            case Opcodes.F2D:

                break;
            case Opcodes.D2I:

                break;
            case Opcodes.D2L:

                break;
            case Opcodes.D2F:

                break;
            case Opcodes.I2B:

                break;
            case Opcodes.I2C:

                break;
            case Opcodes.I2S:

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

                break;
            case Opcodes.LRETURN:

                break;
            case Opcodes.FRETURN:

                break;
            case Opcodes.DRETURN:

                break;
            case Opcodes.ARETURN:

                break;
            case Opcodes.RETURN:

                break;

            // array length
            case Opcodes.ARRAYLENGTH:
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
}

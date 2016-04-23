package util.noop;

import org.objectweb.asm.Opcodes;
import util.Frame;
import util.RefType;
import util.except.UnsupportedOpcodeException;
import util.ref.Arbitrary;

import java.util.function.IntPredicate;

public class ArrayInst {
    private static final IntPredicate isloadInst = opcode -> opcode >= 46 && opcode <= 53;
    private static final IntPredicate isStoreInst = opcode -> opcode >= 79 && opcode <= 86;

    private final Frame frame;

    public ArrayInst(Frame frame) {
        this.frame = frame;
    }

    public void apply(int opcode) {
        if (isloadInst.test(opcode)) {
            // load from array instruction
            RefType type;
            switch (opcode) {
                case Opcodes.IALOAD:
                    type = RefType.INT;
                    break;
                case Opcodes.LALOAD:
                    type = RefType.LONG;
                    break;
                case Opcodes.FALOAD:
                    type = RefType.FLOAT;
                    break;
                case Opcodes.DALOAD:
                    type = RefType.DOUBLE;
                    break;
                case Opcodes.AALOAD:
                    type = RefType.OBJECTREF;
                    break;
                case Opcodes.BALOAD:
                    type = RefType.BOOLEAN;
                    break;
                case Opcodes.CALOAD:
                    type = RefType.CHAR;
                    break;
                case Opcodes.SALOAD:
                    type = RefType.SHORT;
                    break;
                default:
                    throw new UnsupportedOpcodeException(opcode);
            }
            frame.replaceStack(2, Arbitrary.val(type));
        } else if (isStoreInst.test(opcode)){
            // store to array instruction
            frame.replaceStack(3);
        } else throw new UnsupportedOpcodeException(opcode);
    }
}

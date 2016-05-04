package util.noop;


import org.objectweb.asm.Opcodes;
import util.RefType;
import util.except.UnsupportedOpcodeException;
import util.frame.Frame;
import util.ref.Arbitrary;

import java.util.function.IntPredicate;

class ArrayInst {
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
                case Opcodes.BALOAD:
                case Opcodes.CALOAD:
                case Opcodes.SALOAD:
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

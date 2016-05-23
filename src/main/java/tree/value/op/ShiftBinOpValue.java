package tree.value.op;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import tree.value.MyValue;

public class ShiftBinOpValue extends BinOpValue {
    private final static int startOpcode = Opcodes.ISHL;
    private final static Type[] types = {Type.INT_TYPE, Type.LONG_TYPE};
    private final static String[] opSyms = {"<<", ">>", ">>>"};


    public ShiftBinOpValue(int opcode, MyValue a, MyValue b) {
        super(opcode, a, b);
    }

    @Override
    protected Type evalType() {
        return types[(opcode - startOpcode) % types.length];
    }

    public String toString() {
        return showInfixOp(opSyms[(opcode - startOpcode) / types.length]);
    }
}

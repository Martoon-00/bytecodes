package tree.value.op;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import tree.value.MyValue;

import java.util.Set;

public class LogicBinOpValue extends BinOpValue {
    private final static int startOpcode = Opcodes.IAND;
    private final static Type[] types = {Type.INT_TYPE, Type.LONG_TYPE};
    private final static String[] opSyms = {"&", "|", "^"};


    public LogicBinOpValue(int opcode, MyValue a, MyValue b) {
        super(opcode, a, b);
    }

    @Override
    protected Type evalType() {
        return types[(opcode - startOpcode) % types.length];
    }

    public String toString() {
        return showInfixOp(opSyms[(opcode - startOpcode) / opSyms.length]);
    }
}



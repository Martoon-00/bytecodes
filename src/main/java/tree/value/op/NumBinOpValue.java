package tree.value.op;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import scan.ref.Ref;
import tree.value.MyValue;

public class NumBinOpValue extends BinOpValue {
    private final static int startOpcode = Opcodes.IADD;
    private final static Type[] types = {Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE};
    private final static String[] opSyms = {"+", "-", "*", "/", "%"};

    public NumBinOpValue(int opcode, MyValue a, MyValue b) {
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

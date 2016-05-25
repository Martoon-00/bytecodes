package tree.value.op;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import tree.value.MyValue;

public class NegUnaryOpValue extends UnaryOpValue {
    private final static int startOpcode = Opcodes.INEG;
    private final static Type[] types = {Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE};

    public NegUnaryOpValue(int opcode, MyValue a) {
        super(opcode, a);
    }

    @Override
    public Type getType() {
        return types[opcode - startOpcode];
    }

    public String toString() {
        return "(-" + a.toString() + ")";
    }
}

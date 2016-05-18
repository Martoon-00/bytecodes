package scan.ref.op;

import org.objectweb.asm.Opcodes;
import scan.RefType;
import scan.ref.Ref;

public class NegUnaryOpRef extends UnaryOpRef {
    private final static int startOpcode = Opcodes.INEG;
    private final static RefType[] types = {RefType.INT, RefType.LONG, RefType.FLOAT, RefType.DOUBLE};

    public NegUnaryOpRef(int opcode, Ref a) {
        super(opcode, a);
    }

    @Override
    protected RefType evalType() {
        return types[opcode - startOpcode];
    }

    @Override
    protected String show() {
        return "(-" + a.toString() + ")";
    }
}

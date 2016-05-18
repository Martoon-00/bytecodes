package scan.ref.op;

import org.objectweb.asm.Opcodes;
import scan.RefType;
import scan.ref.Ref;

public class ShiftBinOpRef extends BinOpRef {
    private final static int startOpcode = Opcodes.ISHL;
    private final static RefType[] types = {RefType.INT, RefType.LONG};
    private final static String[] opSyms = {"<<", ">>", ">>>"};


    public ShiftBinOpRef(int opcode, Ref a, Ref b) {
        super(opcode, a, b);
    }

    @Override
    protected RefType evalType() {
        return types[(opcode - startOpcode) % types.length];
    }

    @Override
    protected String show() {
        return showInfixOp(opSyms[(opcode - startOpcode) / types.length]);
    }
}

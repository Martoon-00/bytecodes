package scan.ref.op;

import scan.RefType;
import scan.except.UnsupportedOpcodeException;
import scan.ref.Ref;

public abstract class BinOpRef extends Ref {
    protected final int opcode;
    protected final Ref a;
    protected final Ref b;

    public BinOpRef(int opcode, Ref a, Ref b) {
        super(null);
        this.opcode = opcode;
        this.a = a;
        this.b = b;
    }

    public static BinOpRef of(int opcode, Ref a, Ref b) {
        //  96 - 115:  (+, -, *, /, %)
        // 120 - 125:  (<<, >>, >>>)
        // 126 - 131:  (&, |, ^)
        // 148 - 152:  extended comparisons
        if (opcode >= 96 && opcode < 116) {
            return new NumBinOpRef(opcode, a, b);
        } else if (opcode >= 120 && opcode < 126) {
            return new ShiftBinOpRef(opcode, a, b);
        } else if (opcode >= 126 && opcode < 131) {
            return new LogicBinOpRef(opcode, a, b);
        } else if (opcode >= 148 && opcode < 153) {
            return new CmpBinOpRef(opcode, a, b);
        } else throw new UnsupportedOpcodeException(opcode);
    }

    @Override
    public final RefType getType() {
        return evalType();
    }

    protected abstract RefType evalType();

    protected String showInfixOp(String op) {
        return String.format("(%s %s %s)", a, op, b);
    }

    protected String showPrefixOp(String op) {
        return String.format("%s (%s, %s)", op, a, b);
    }

}

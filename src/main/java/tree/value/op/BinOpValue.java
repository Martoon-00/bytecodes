package tree.value.op;

import org.objectweb.asm.Type;
import scan.except.UnsupportedOpcodeException;
import tree.value.MyValue;

import java.util.Set;

public abstract class BinOpValue extends MyValue {
    protected final int opcode;
    protected final MyValue a;
    protected final MyValue b;

    public BinOpValue(int opcode, MyValue a, MyValue b) {
        super(null);
        this.opcode = opcode;
        this.a = a;
        this.b = b;
    }

    public static BinOpValue of(int opcode, MyValue a, MyValue b) {
        //  96 - 115:  (+, -, *, /, %)
        // 120 - 125:  (<<, >>, >>>)
        // 126 - 131:  (&, |, ^)
        // 148 - 152:  extended comparisons
        // 159 - 166:  conditional jumps
        if (opcode >= 96 && opcode < 116) {
            return new NumBinOpValue(opcode, a, b);
        } else if (opcode >= 120 && opcode < 126) {
            return new ShiftBinOpValue(opcode, a, b);
        } else if (opcode >= 126 && opcode < 131) {
            return new LogicBinOpValue(opcode, a, b);
        } else if (opcode >= 148 && opcode < 153) {
            return new CmpBinOpValue(opcode, a, b);
        } else if (opcode >= 159 && opcode < 167) {
            return null;
        } else throw new UnsupportedOpcodeException(opcode);
    }

    @Override
    public final Type getType() {
        return evalType();
    }

    protected abstract Type evalType();

    protected String showInfixOp(String op) {
        return String.format("(%s %s %s)", a, op, b);
    }

    protected String showPrefixOp(String op) {
        return String.format("%s (%s, %s)", op, a, b);
    }

    @Override
    protected MyValue proceedElimRec(Set<MyValue> visited, boolean complicated) {
        MyValue a2 = a.eliminateRecursion(visited, true);
        MyValue b2 = b.eliminateRecursion(visited, true);
        return BinOpValue.of(opcode, a2, b2);
    }
}
package tree.value.op;

import inter.InterContext;
import org.objectweb.asm.Type;
import scan.except.UnsupportedOpcodeException;
import tree.value.AltValue;
import tree.value.AnyValue;
import tree.value.ConstValue;
import tree.value.MyValue;

import java.util.Map;
import java.util.stream.Collectors;

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
    public abstract Type getType();

    protected String showInfixOp(String op) {
        return String.format("(%s %s %s)", a, op, b);
    }

    protected String showPrefixOp(String op) {
        return String.format("%s (%s, %s)", op, a, b);
    }

    @Override
    protected MyValue proceedElimRec(Map<MyValue, Boolean> visited) {
        Map<MyValue, Boolean> complicatedVis = visited.keySet().stream()
                .collect(Collectors.toMap(k -> k, k -> Boolean.TRUE));
        MyValue a2 = a.eliminateRecursion(complicatedVis);
        MyValue b2 = b.eliminateRecursion(complicatedVis);
        if (complicatedVis.values().stream().anyMatch(v -> !v))
            throw new Error("Something goes wrong");
        return BinOpValue.of(opcode, a2, b2);
    }

    @Override
    public MyValue simplify() {
        MyValue a2 = a.simplify();
        MyValue b2 = b.simplify();
        if (a2 instanceof AnyValue || b2 instanceof AnyValue)
            return AnyValue.of(getType());
        if (a2 instanceof ConstValue && b2 instanceof ConstValue)
            return evaluate(((ConstValue) a2), ((ConstValue) b2));

        boolean ad = AltValue.isConstSet(a2);
        boolean bd = AltValue.isConstSet(b2);
        if (ad && bd) {
            return AltValue.of(((AltValue) a2).getAlternatives().flatMap(av ->
                    ((AltValue) b2).getAlternatives().map(bv -> evaluate(((ConstValue) av), (ConstValue) bv))
            ).toArray(MyValue[]::new));
        }
        if (ad) {
            return AltValue.of(((AltValue) a2).getAlternatives()
                    .map(av -> evaluate(((ConstValue) av), ((ConstValue) b2)))
                    .toArray(MyValue[]::new));
        }
        if (bd) {
            return AltValue.of(((AltValue) b2).getAlternatives()
                    .map(bv -> evaluate(((ConstValue) bv), ((ConstValue) a2)))
                    .toArray(MyValue[]::new));
        }
        return BinOpValue.of(opcode, a2, b2);
    }

    protected abstract MyValue evaluate(ConstValue a, ConstValue b);

    @Override
    public MyValue resolveReferences(InterContext context, int depth) {
        MyValue a2 = this.a.resolveReferences(context, depth);
        MyValue b2 = this.b.resolveReferences(context, depth);
        return BinOpValue.of(opcode, a2, b2);
    }

    @Override
    public MyValue eliminateReferences() {
        MyValue a2 = this.a.eliminateReferences();
        MyValue b2 = this.b.eliminateReferences();
        return BinOpValue.of(opcode, a2, b2);
    }

    @Override
    public MyValue copy() {
        return BinOpValue.of(opcode, a.copy(), b.copy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BinOpValue that = (BinOpValue) o;

        if (opcode != that.opcode) return false;
        if (!a.equals(that.a)) return false;
        return b.equals(that.b);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + opcode;
        result = 31 * result + a.hashCode();
        result = 31 * result + b.hashCode();
        return result;
    }
}

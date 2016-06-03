package tree.value.op;

import inter.InterContext;
import org.objectweb.asm.Type;
import scan.except.UnsupportedOpcodeException;
import tree.value.AnyValue;
import tree.value.ConstValue;
import tree.value.MyValue;

import java.util.Set;

public abstract class UnaryOpValue extends MyValue {
    protected final int opcode;
    protected final MyValue a;

    public UnaryOpValue(int opcode, MyValue a) {
        super(null);
        this.opcode = opcode;
        this.a = a;
    }

    public static UnaryOpValue of(int opcode, MyValue a) {
        // 116 - 119:  unary -
        // 136 - 147:  casts
        if (opcode >= 116 && opcode < 120) {
            return new NegUnaryOpValue(opcode, a);
        } else if (opcode >= 133 && opcode < 148) {
            return new CastUnaryOpValue(opcode, a);
        } else throw new UnsupportedOpcodeException(opcode);
    }

    public static boolean isUnaryOp(int opcode) {
        return opcode >= 116 && opcode < 120
                || opcode >= 133 && opcode < 148;
    }

    @Override
    public abstract Type getType();

    @Override
    protected MyValue proceedElimRec(Set<MyValue> visited, boolean complicated) {
        MyValue a2 = a.eliminateRecursion(visited, true);
        return UnaryOpValue.of(opcode, a2);
    }

    @Override
    public MyValue simplify() {
        MyValue a2 = a.simplify();
        if (a2 instanceof AnyValue)
            return AnyValue.of(getType());
        if (a2 instanceof ConstValue)
            return evaluate(((ConstValue) a2));
        return UnaryOpValue.of(opcode, a2);
    }

    protected abstract MyValue evaluate(ConstValue a);

    @Override
    public MyValue resolveReferences(InterContext context, int depth) {
        return UnaryOpValue.of(opcode, a.resolveReferences(context, depth));
    }

    @Override
    public MyValue eliminateReferences() {
        return UnaryOpValue.of(opcode, a.eliminateReferences());
    }

    @Override
    public MyValue copy() {
        return UnaryOpValue.of(opcode, a.copy());
    }
}

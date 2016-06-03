package tree.value;

import com.sun.istack.internal.NotNull;
import inter.InterContext;
import org.objectweb.asm.Type;

import java.util.Set;
import java.util.function.Function;

public class LinkValue extends MyValue implements Replaceable {
    private MyValue value;

    protected LinkValue(@NotNull MyValue value) {
        super(value.getType());
        this.value = value;
    }

    public static LinkValue of(MyValue value) {
        if (value == null)
            return null;
        return new LinkValue(value);
    }

    @Override
    public Type getType() {
        return value.getType();
    }

    public MyValue getValue() {
        return value;
    }

    @Override
    public void replaceEntry(Function<MyValue, MyValue> map) {
        value = map.apply(value);
    }

    @Override
    protected MyValue proceedElimRec(Set<MyValue> visited, boolean complicated) {
        // TODO: not LinkValue.of(...)? Join with simplify?
        value = value.eliminateRecursion(visited, complicated);
        return this;
    }

    @Override
    public MyValue simplify() {
        return value.simplify();
    }

    @Override
    public MyValue resolveReferences(InterContext context, int depth) {
        value = value.resolveReferences(context, depth);
        return this;
    }

    @Override
    public MyValue eliminateReferences() {
        value = value.eliminateReferences();
        return this;
    }

    @Override
    public MyValue copy() {
        throw new UnsupportedOperationException();
    }

    public static boolean sameValue(MyValue v1, MyValue v2) {
        for (MyValue v = v1; v instanceof LinkValue; v = ((LinkValue) v).getValue())
            if (v == v2) return true;
        for (MyValue v = v2; v instanceof LinkValue; v = ((LinkValue) v).getValue())
            if (v == v1) return true;
        return false;
    }

    @Override
    public String toString() {
        return "^" + value;
    }
}

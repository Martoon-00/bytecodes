package tree.value;

import com.sun.istack.internal.NotNull;
import org.objectweb.asm.Type;

import java.util.Set;
import java.util.function.Function;

public class ReplaceableValue extends MyValue implements Replaceable {
    private MyValue value;

    private ReplaceableValue(@NotNull MyValue value) {
        super(value.getType());
        this.value = value;
    }

    public static ReplaceableValue of(MyValue value) {
        if (value == null)
            return null;
        return new ReplaceableValue(value);
    }

    @Override
    public Type getType() {
        return value.getType();
    }

    @Override
    public int getSize() {
        return value.getSize();
    }

    @Override
    public boolean isReference() {
        return value.isReference();
    }

    @Override
    public void replaceEntry(Function<MyValue, MyValue> map) {
        value = map.apply(value);
    }

    @Override
    protected MyValue proceedElimRec(Set<MyValue> visited, boolean complicated) {
        return value.eliminateRecursion(visited, complicated);
    }

    @Override
    public boolean equals(Object o) {
        MyValue v = value;
        while (v instanceof ReplaceableValue)
            v = ((ReplaceableValue) v).value;
        return o.equals(v);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "^" + value;
    }
}

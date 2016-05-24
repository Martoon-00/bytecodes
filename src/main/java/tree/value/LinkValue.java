package tree.value;

import com.sun.istack.internal.NotNull;
import org.objectweb.asm.Type;

import java.util.Set;
import java.util.function.Function;

public class LinkValue extends MyValue implements Replaceable {
    private MyValue value;

    private LinkValue(@NotNull MyValue value) {
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
    public MyValue simplify() {
//        if (value instanceof AnyValue || value instanceof NoValue)
//            return value;
//        return new LinkValue(value);
        return value;
    }

    @Override
    public String toString() {
        return "^" + value;
    }
}

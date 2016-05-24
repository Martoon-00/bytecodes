package tree.value;

import com.sun.istack.internal.NotNull;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.BasicValue;

import java.util.Set;

public class MyBasicValue extends MyValue {
    @NotNull
    private final BasicValue value;

    private MyBasicValue(@NotNull BasicValue value) {
        // TODO: composition -> inheritance?
        super(null);
        this.value = value;
    }

    public static MyBasicValue of(BasicValue value) {
        if (value == null)
            return null;
        return new MyBasicValue(value);
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
    protected MyValue proceedElimRec(Set<MyValue> visited, boolean complicated) {
        if (value instanceof MyValue) {
            return ((MyValue) value).eliminateRecursion(visited, complicated);
        } else {
            return this;
        }
    }

    @Override
    public MyValue simplify() {
        return this;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

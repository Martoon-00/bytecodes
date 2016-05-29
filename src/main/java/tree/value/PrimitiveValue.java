package tree.value;

import org.objectweb.asm.Type;

import java.util.Set;

public abstract class PrimitiveValue extends MyValue {
    public PrimitiveValue(Type type) {
        super(type);
    }

    @Override
    protected MyValue proceedElimRec(Set<MyValue> visited, boolean complicated) {
        return this;
    }

    @Override
    public MyValue simplify() {
        return this;
    }

    @Override
    public MyValue copy() {
        return this;
    }
}

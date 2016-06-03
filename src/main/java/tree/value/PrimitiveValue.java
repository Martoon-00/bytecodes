package tree.value;

import org.objectweb.asm.Type;

import java.util.Map;

public abstract class PrimitiveValue extends MyValue {
    public PrimitiveValue(Type type) {
        super(type);
    }

    @Override
    protected MyValue proceedElimRec(Map<MyValue, Boolean> visited) {
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

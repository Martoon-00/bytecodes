package tree.value;

import org.objectweb.asm.Type;

import java.util.Set;

public class FinalValue extends MyValue {
    public FinalValue(Type type) {
        super(type);
    }

    @Override
    protected MyValue proceedElimRec(Set<MyValue> visited, boolean complicated) {
        return this;
    }
}

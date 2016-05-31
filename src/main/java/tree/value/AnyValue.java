package tree.value;

import intra.IntraContext;
import org.objectweb.asm.Type;

public class AnyValue extends PrimitiveValue {
    private AnyValue(Type type) {
        super(type);
    }

    public static AnyValue of(Type type) {
        return type == null ? null : new AnyValue(type);
    }

    @Override
    public MyValue resolveReferences(IntraContext context, int depth) {
        return this;
    }

    @Override
    public MyValue eliminateReferences() {
        return this;
    }

    @Override
    public String toString() {
        return "*";
    }
}

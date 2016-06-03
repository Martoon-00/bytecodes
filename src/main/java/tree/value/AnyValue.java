package tree.value;

import inter.InterContext;
import org.objectweb.asm.Type;

public class AnyValue extends PrimitiveValue {
    protected AnyValue(Type type) {
        super(type);
    }

    public static AnyValue of(Type type) {
        return type == null ? null : new AnyValue(type);
    }

    @Override
    public MyValue resolveReferences(InterContext context, int depth) {
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

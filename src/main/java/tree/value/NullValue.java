package tree.value;

import inter.InterContext;
import org.objectweb.asm.Type;

public class NullValue extends PrimitiveValue {

    public NullValue(Type type) {
        super(type);
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
        return "<null>";
    }
}

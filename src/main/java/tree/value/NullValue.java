package tree.value;

import intra.IntraContext;
import org.objectweb.asm.Type;

public class NullValue extends PrimitiveValue {

    public NullValue(Type type) {
        super(type);
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
        return "<null>";
    }
}

package tree.value;

import intra.IntraContext;
import org.objectweb.asm.Type;

public class ThisValue extends PrimitiveValue {
    public ThisValue() {
        super(Type.getObjectType("java/lang/Object"));
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
    public boolean equals(Object value) {
        return value instanceof ThisValue;
    }

    @Override
    public int hashCode() {
        return 345237654;
    }

    @Override
    public String toString() {
        return "<this>";
    }
}

package tree.value;

import org.objectweb.asm.Type;

public class AnyValue extends FinalValue {
    public AnyValue(Type type) {
        super(type);
    }

    @Override
    public String toString() {
        return "*";
    }
}

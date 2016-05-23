package tree.value;

import org.objectweb.asm.Type;

public class NoValue extends FinalValue {
    public NoValue(Type type) {
        super(type);
    }

    @Override
    public String toString() {
        return "<no value>";
    }
}

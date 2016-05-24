package tree.value;

import org.objectweb.asm.Type;

import java.util.Objects;

public class ConstValue extends FinalValue {
    private final Object value;

    public ConstValue(Type type, Object value) {
        super(type);
        Objects.requireNonNull(value);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return value.equals(((ConstValue) o).value);

    }

    @Override
    public int hashCode() {
        return 435323 + 31 * value.hashCode();
    }

    @Override
    public String toString() {
        return "ConstValue{ " + value + " }";
    }
}

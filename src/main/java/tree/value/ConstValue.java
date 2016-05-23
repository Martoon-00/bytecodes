package tree.value;

import org.objectweb.asm.Type;

public class ConstValue extends FinalValue {
    private final Object value;

    public ConstValue(Type type, Object value) {
        super(type);
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ConstValue constValue = (ConstValue) o;

        return value != null ? value.equals(constValue.value) : constValue.value == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ConstValue{ " + value + " }";
    }
}

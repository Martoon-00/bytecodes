package tree.value;

public class NoValue extends FinalValue {
    public NoValue() {
        super(null);
    }

    @Override
    public String toString() {
        return "<no value>";
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o != null && getClass() == o.getClass());
    }

    @Override
    public int hashCode() {
        return 1231456;
    }

}

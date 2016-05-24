package tree.value;

public class NoValue extends FinalValue {
    public NoValue() {
        super(null);
    }

    @Override
    public String toString() {
        return "<no value>";
    }
}

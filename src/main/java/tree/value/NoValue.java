package tree.value;

import inter.InterContext;

public class NoValue extends PrimitiveValue {
    public NoValue() {
        super(null);
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

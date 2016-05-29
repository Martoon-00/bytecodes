package tree.value;

import intra.IntraContext;

public class NoValue extends PrimitiveValue {
    public NoValue() {
        super(null);
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

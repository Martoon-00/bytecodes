package util.ref;

import util.RefType;

public abstract class Ref {
    protected final RefType type;
    protected boolean invalid;

    public Ref(RefType type) {
        this.type = type;
    }

    public RefType getType() {
        return type;
    }

    public void invalidate() {
        invalid = true;
    }

    protected abstract String show();

    @Override
    public final String toString() {
        return invalid ? Arbitrary.val(type).toString()
                : show();
    }
}

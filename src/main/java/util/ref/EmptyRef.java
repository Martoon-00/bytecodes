package util.ref;

import util.RefType;

public class EmptyRef extends Ref {
    public EmptyRef() {
        super(null);
    }

    @Override
    public RefType getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String show() {
        return "EmptyRef{}";
    }
}

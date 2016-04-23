package util.ref;

import util.RefType;

public abstract class Ref {
    private final RefType type;

    public Ref(RefType type) {
        this.type = type;
    }

    public RefType getType() {
        return type;
    }
}

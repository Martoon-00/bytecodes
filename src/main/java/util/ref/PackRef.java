package util.ref;

import util.RefType;

public class PackRef extends Ref {
    private final Ref[] values;

    private PackRef(RefType type, Ref[]values) {
        super(type);
        this.values = values;
    }

    public static PackRef of(RefType type, Ref... values) {
        return new PackRef(type, values);
    }

    public Ref[] getValues() {
        return values;
    }
}

package util.ref;

import util.RefType;
import util.except.InvalidBytecodeException;

public class PackRef extends Ref {
    private final Ref[] values;

    private PackRef(RefType type, Ref[] values) {
        super(type);
        this.values = values;
    }

    public static PackRef of(RefType type, Ref... values) {
        return new PackRef(type, values);
    }

    public static Ref ofTwo(Ref a, Ref b) {
        if (a == b) {
            return a;
        }
        if (!a.getType().equals(b.type))
            throw new InvalidBytecodeException("Values has different types: " + a.getType() + " vs " + b.getType());

        return PackRef.of(a.getType(), a, b);
    }

    public Ref[] getValues() {
        return values;
    }
}

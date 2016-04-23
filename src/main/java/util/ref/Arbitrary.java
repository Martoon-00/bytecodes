package util.ref;

import util.RefType;

public class Arbitrary extends FinalRef {
    private Arbitrary(RefType type) {
        super(type);
    }

    public static Arbitrary val(RefType type) {
        return new Arbitrary(type);
    }
}

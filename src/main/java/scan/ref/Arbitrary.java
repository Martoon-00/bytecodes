package scan.ref;

import scan.RefType;

public class Arbitrary extends FinalRef {
    private Arbitrary(RefType type) {
        super(type);
    }

    public static Arbitrary val(RefType type) {
        return new Arbitrary(type);
    }

    @Override
    public void invalidate() {
    }

    @Override
    protected String show() {
        return "Arbitrary";
    }

    public static class ArbitraryVal implements SpecValue {
        private final static ArbitraryVal val = new ArbitraryVal();
    }
}

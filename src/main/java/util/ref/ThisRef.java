package util.ref;

import util.RefType;

public class ThisRef extends FinalRef {
    private ThisRef() {
        super(RefType.OBJECTREF);
    }

    public static ThisRef val() {
        return new ThisRef();
    }

    @Override
    protected String show() {
        return "<this>";
    }

    public static class ThisVal implements SpecValue {
        private static final ThisRef val = new ThisRef();
    }
}

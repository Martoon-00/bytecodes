package util.ref;

import util.RefType;

public class ThisRef extends FinalRef {
    private ThisRef() {
        super(RefType.OBJECTREF);
    }

    public static ThisRef val() {
        return new ThisRef();
    }
}

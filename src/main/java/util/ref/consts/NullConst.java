package util.ref.consts;

import util.RefType;
import util.ref.Ref;

public class NullConst extends Ref {
    private NullConst() {
        super(RefType.OBJECTREF);
    }

    public static NullConst val() {
        return new NullConst();
    }

    @Override
    protected String show() {
        return "<null>";
    }
}

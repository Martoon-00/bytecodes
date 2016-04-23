package util.ref.consts;

import util.RefType;
import util.ref.EvalRef;

public class NullConst extends EvalRef {
    private NullConst() {
        super(RefType.OBJECTREF);
    }

    public static NullConst val() {
        return new NullConst();
    }

    @Override
    public Object value() {
        return NullObj.val();
    }

}

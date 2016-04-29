package util.ref.consts;

import util.RefType;
import util.except.UndefinedValueException;
import util.ref.EvalRef;

public class NoRef extends EvalRef {
    private final String msg;

    public NoRef() {
        this(null);
    }

    public NoRef(String errorMsgOnAccess) {
        super(null);
        this.msg = errorMsgOnAccess;
    }

    @Override
    public Object value() {
        throw new UndefinedValueException(msg);
    }

    @Override
    public RefType getType() {
        throw new UndefinedValueException(msg);
    }
}

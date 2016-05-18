package scan.ref.consts;

import scan.RefType;
import scan.except.UndefinedValueException;
import scan.ref.Ref;

public class NoRef extends Ref {
    private final String msg;

    public NoRef() {
        this(null);
    }

    private NoRef(String msg) {
        super(null);
        this.msg = msg;
    }

    public static Ref of(String errorMsgOnAccess) {
        return new NoRef(errorMsgOnAccess);
    }

    @Override
    public RefType getType() {
        throw new UndefinedValueException(msg);
    }

    @Override
    public void invalidate() {
        throw new UndefinedValueException(msg);
    }

    @Override
    protected String show() {
        return "<No ref>";
    }
}
package util.ref.consts;

import util.RefType;
import util.ref.EvalRef;

public class Const extends EvalRef {
    private final Object value;

    public Const(Object value, RefType type) {
        super(type);
        this.value = value;
    }

    @Override
    public Object value() {
        return value;
    }
}

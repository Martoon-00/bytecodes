package util.ref.consts;

import util.RefType;
import util.ref.FinalRef;

public class Const extends FinalRef {
    private final Object value;

    public Const(Object value, RefType type) {
        super(type);
        this.value = value;
    }

    @Override
    protected String show() {
        return "Const{ " + value + " }";
    }
}

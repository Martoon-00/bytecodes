package scan.ref.consts;

import scan.RefType;
import scan.ref.FinalRef;

public class Const extends FinalRef {
    private final Object value;

    public Const(Object value, RefType type) {
        super(type);
        this.value = value;
    }

    @Override
    protected String show() {
        if (value instanceof String)
            return "\"" + value + "\"";

        return value.toString();
    }
}

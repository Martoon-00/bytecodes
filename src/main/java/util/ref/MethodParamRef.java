package util.ref;

import util.MethodRef;
import util.RefType;

public class MethodParamRef extends Ref {
    private final MethodRef method;
    private final int index;

    public MethodParamRef(MethodRef method, int index, RefType type) {
        super(type);
        this.method = method;
        this.index = index;
    }

    public MethodRef getMethod() {
        return method;
    }

    public int getIndex() {
        return index;
    }
}

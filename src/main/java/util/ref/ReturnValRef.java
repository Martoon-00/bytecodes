package util.ref;

import util.MethodRef;
import util.RefType;

import java.util.List;

public class ReturnValRef extends Ref {
    private final MethodRef method;
    private final List<Ref> params;

    public ReturnValRef(MethodRef method, List<Ref> params, RefType type) {
        super(type);
        this.method = method;
        this.params = params;
    }
}
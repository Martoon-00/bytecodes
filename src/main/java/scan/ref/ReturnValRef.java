package scan.ref;

import scan.MethodRef;
import scan.RefType;

import java.util.List;

public class ReturnValRef extends Ref {
    private final MethodRef method;
    private final List<Ref> params;

    public ReturnValRef(MethodRef method, List<Ref> params, RefType type) {
        super(type);
        this.method = method;
        this.params = params;
    }

    @Override
    protected String show() {
        return String.format("ReturnVal(%s){ %s }", method, params);
    }
}

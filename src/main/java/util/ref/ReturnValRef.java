package util.ref;

import java.util.List;

public class ReturnValRef {
    private final MethodRef method;
    private final List<Ref> params;

    public ReturnValRef(MethodRef method, List<Ref> params) {
        this.method = method;
        this.params = params;
    }
}

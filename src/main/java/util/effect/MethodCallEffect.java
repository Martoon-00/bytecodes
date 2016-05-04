package util.effect;

import util.MethodRef;
import util.ref.Ref;

import java.util.List;

public class MethodCallEffect implements Effect {
    private final MethodRef method;
    private final List<Ref> params;

    public MethodCallEffect(MethodRef method, List<Ref> params) {
        this.method = method;
        this.params = params;
    }

    @Override
    public String toString() {
        return "MethodCallEffect{ " +  method +"( " + params + " ) } ";
    }

    public MethodRef getMethod() {
        return method;
    }

    public List<Ref> getParams() {
        return params;
    }
}

package scan.effect;

import scan.MethodRef;
import scan.ref.Ref;

import java.util.List;

public class MethodCallEffect implements Effect {
    private final MethodRef caller;
    private final MethodRef callee;
    private final List<Ref> params;

    public MethodCallEffect(MethodRef caller, MethodRef callee, List<Ref> params) {
        this.caller = caller;
        this.callee = callee;
        this.params = params;
    }

    @Override
    public String toString() {
        return "MethodCallEffect{ " + caller + " -> " + callee +"( " + params + " ) } ";
    }

    public MethodRef getCallee() {
        return callee;
    }

    public List<Ref> getParams() {
        return params;
    }
}

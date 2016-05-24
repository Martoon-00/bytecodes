package tree.effect;

import scan.MethodRef;
import tree.value.MyValue;

import java.util.List;

public class MethodCallEffect {
    private final MethodRef caller;
    private final MethodRef callee;
    private final List<MyValue> params;

    public MethodCallEffect(MethodRef caller, MethodRef callee, List<MyValue> params) {
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

    public List<MyValue> getParams() {
        return params;
    }

    public void simplify() {
        for (int i = 0; i < params.size(); i++) {
            params.set(i, params.get(i).eliminateRecursion().simplify());
        }
    }
}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodCallEffect that = (MethodCallEffect) o;

        if (!caller.equals(that.caller)) return false;
        if (!callee.equals(that.callee)) return false;
        return params.equals(that.params);

    }

    @Override
    public int hashCode() {
        int result = caller.hashCode();
        result = 31 * result + callee.hashCode();
        result = 31 * result + params.hashCode();
        return result;
    }
}

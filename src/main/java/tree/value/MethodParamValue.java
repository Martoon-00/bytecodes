package tree.value;

import intra.IntraContext;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.BasicValue;
import scan.MethodRef;
import tree.effect.MethodCallEffect;
import tree.exc.AnalyzerRuntimeException;

import java.util.List;
import java.util.Set;

public class MethodParamValue extends LinkValue {
    private final MethodRef method;
    private final int index;
    private boolean resolved;

    public MethodParamValue(MethodRef method, int index, Type type) {
        super(MyBasicValue.of(new BasicValue(type)));
        this.method = method;
        this.index = index;
    }

    public MethodRef getMethod() {
        return method;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public MyValue resolveReferences(IntraContext context, int depth) {
        if (!resolved) {  // if already resolved, we can be in recursion
            resolved = true;
            MyValue[] values = context.getCallEffects(method).stream()
                    .map(this::getParamFromEffect)
                    .map(MyValue::copy)
                    .map(param -> depth == 0 ? param : param.resolveReferences(context, depth - 1))
                    .toArray(MyValue[]::new);
            replaceEntry(was -> LinkValue.of(AltValue.of(values)));
        }
        return this;
    }

    private MyValue getParamFromEffect(MethodCallEffect effect) {
        List<MyValue> params = effect.getParams();
        if (params.size() <= index)
            throw new AnalyzerRuntimeException(
                    String.format("Method %s passes at least %d parameters to %s, but only %d is expected",
                    effect.getCaller(), index, effect.getCallee(), params.size()));
        return params.get(index);
    }

    @Override
    protected MyValue proceedElimRec(Set<MyValue> visited, boolean complicated) {
        return resolved ? super.proceedElimRec(visited, complicated) : this;
    }

    @Override
    public MyValue simplify() {
        return resolved ? super.simplify() : this;
    }

    @Override
    public MyValue eliminateReferences() {
        return resolved ? getValue().eliminateReferences() : new AnyValue(getType());
    }

    @Override
    public MyValue copy() {
        if (resolved)
            throw new IllegalStateException("Already resolved");
        return new MethodParamValue(method, index, getType());
    }

    public String toString() {
        String entry = resolved ? " {" + getValue().toString() + "}" : "";
        return "MethodParamRef{ " + method + " #" + index + entry + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodParamValue that = (MethodParamValue) o;

        if (index != that.index) return false;
        return method.equals(that.method);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + method.hashCode();
        result = 31 * result + index;
        return result;
    }
}

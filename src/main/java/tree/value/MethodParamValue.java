package tree.value;

import intra.IntraContext;
import org.objectweb.asm.Type;
import scan.MethodRef;
import tree.effect.MethodCallEffect;
import tree.exc.AnalyzerRuntimeException;

import java.util.List;

public class MethodParamValue extends ResolvableValue {
    private final MethodRef method;
    private final int index;

    public MethodParamValue(MethodRef method, int index, Type type) {
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

    protected MyValue resolveUnresolved(IntraContext context, int depth) {
        return AltValue.of(context.getCallEffects(method).stream()
                .map(this::getParamFromEffect)
                .map(MyValue::copy)
                .map(param -> depth == 0 ? param : param.resolveReferences(context, depth - 1))
                .toArray(MyValue[]::new));
    }

    private MyValue getParamFromEffect(MethodCallEffect effect) {
        List<MyValue> params = effect.getParams();
        if (params.size() <= index)
            throw new AnalyzerRuntimeException(
                    String.format("Method %s passes at least %d parameters to %s, but only %d is expected",
                    effect.getCaller(), index, effect.getCallee(), params.size()));
        return params.get(index);
    }

    protected MyValue copyUnresolved() {
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

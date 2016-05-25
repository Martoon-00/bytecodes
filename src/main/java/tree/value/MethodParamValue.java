package tree.value;

import org.objectweb.asm.Type;
import scan.MethodRef;

public class MethodParamValue extends FinalValue {
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

    public String toString() {
        return "MethodParamRef{ " + method + ": " + index + " }";
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

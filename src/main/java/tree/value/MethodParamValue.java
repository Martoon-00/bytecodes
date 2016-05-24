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
}

package util.ref;

public class MethodParamRef implements Ref {
    private final MethodRef method;
    private final int index;

    public MethodParamRef(MethodRef method, int index) {
        this.method = method;
        this.index = index;
    }

    public MethodRef getMethod() {
        return method;
    }

    public int getIndex() {
        return index;
    }
}

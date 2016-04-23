package util.ref;

public class UnaryOpRef implements Ref {
    private final UnaryOp op;
    private final Ref a;

    public UnaryOpRef(UnaryOp op, Ref a) {
        this.op = op;
        this.a = a;
    }

    @Override
    public Object value() {
        return op.apply(a.value());
    }
}

package util.ref;

public class BinOpRef implements Ref {
    private final BinOp op;
    private final Ref a;
    private final Ref b;

    public BinOpRef(BinOp op, Ref a, Ref b) {
        this.op = op;
        this.a = a;
        this.b = b;
    }

    @Override
    public Object value() {
        return op.apply(a.value(), b.value());
    }
}

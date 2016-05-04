package util.ref;

import util.RefType;
import util.op.BinOpType;

public class BinOpRef extends Ref {
    private final BinOpType opType;
    private final Ref a;
    private final Ref b;

    public BinOpRef(BinOpType opType, Ref a, Ref b, RefType type) {
        super(type);
        this.opType = opType;
        this.a = a;
        this.b = b;
    }

    public static BinOpRef of(BinOpType opType, Ref a, Ref b, RefType type) {
        // TODO: shortcut
        return new BinOpRef(opType, a, b, type);
    }

    @Override
    protected String show() {
        return String.format("BinOp{ %s, %s }", a, b);
    }
}

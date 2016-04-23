package util.ref;

import util.RefType;
import util.op.BinOp;

public class BinOpRef extends Ref {
    private final BinOp op;
    private final Ref a;
    private final Ref b;

    private BinOpRef(BinOp op, Ref a, Ref b, RefType type) {
        super(type);
        this.op = op;
        this.a = a;
        this.b = b;
    }

    public static BinOpRef of(BinOp op, Ref a, Ref b, RefType type) {
        // TODO: shortcut
        return new BinOpRef(op, a, b, type);
    }

}

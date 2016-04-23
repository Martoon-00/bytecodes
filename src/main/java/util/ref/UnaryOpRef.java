package util.ref;

import util.RefType;
import util.op.UnaryOp;

public class UnaryOpRef extends Ref {
    private final UnaryOp op;
    private final Ref a;

    private UnaryOpRef(UnaryOp op, Ref a, RefType type) {
        super(type);
        this.op = op;
        this.a = a;
    }

    public static UnaryOpRef of(UnaryOp op, Ref a, RefType type) {
        // TODO: shortcut if possible
        return new UnaryOpRef(op, a, type);
    }

}

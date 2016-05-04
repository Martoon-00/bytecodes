package util.ref;

import util.RefType;
import util.op.UnaryOpType;

public class UnaryOpRef extends Ref {
    private final UnaryOpType opType;
    private final Ref a;

    private UnaryOpRef(UnaryOpType opType, Ref a, RefType type) {
        super(type);
        this.opType = opType;
        this.a = a;
    }

    public static UnaryOpRef of(UnaryOpType opType, Ref a, RefType type) {
        // TODO: shortcut if possible
        return new UnaryOpRef(opType, a, type);
    }

    @Override
    protected String show() {
        return String.format("UnaryOp{ %s }", a);
    }
}

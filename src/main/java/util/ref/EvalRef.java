package util.ref;

import util.RefType;

public abstract class EvalRef extends FinalRef {
    public EvalRef(RefType type) {
        super(type);
    }

    public abstract Object value();
}

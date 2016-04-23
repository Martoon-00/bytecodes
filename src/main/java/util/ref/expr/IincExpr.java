package util.ref.expr;

import util.ref.Ref;

public class IincExpr implements Expr {
    private final Ref ref;
    private final int inc;

    public IincExpr(Ref ref, int inc) {
        this.ref = ref;
        this.inc = inc;
    }

}

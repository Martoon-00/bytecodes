package util.ref.consts;

import util.ref.Ref;

public class NullConst implements Ref {
    private NullConst() {
    }

    public static NullConst val() {
        return new NullConst();
    }
}

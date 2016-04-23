package util.ref.consts;

public class NullConst implements Const {
    private NullConst() {
    }

    public static NullConst val() {
        return new NullConst();
    }
}

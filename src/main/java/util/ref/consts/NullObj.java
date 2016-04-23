package util.ref.consts;

public class NullObj {
    private NullObj() {
    }

    public static NullObj val() {
        return new NullObj();
    }
}

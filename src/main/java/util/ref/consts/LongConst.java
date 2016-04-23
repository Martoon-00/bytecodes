package util.ref.consts;

public class LongConst implements Const {
    private final long value;

    private LongConst(long value) {
        this.value = value;
    }

    public static LongConst of(long value) {
        return new LongConst(value);
    }

    public long getValue() {
        return value;
    }
}

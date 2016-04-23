package util.ref.consts;

public class IntConst implements Const {
    private final int value;

    private IntConst(int value) {
        this.value = value;
    }

    public static IntConst of(int value) {
        return new IntConst(value);
    }

    public int getValue() {
        return value;
    }
}

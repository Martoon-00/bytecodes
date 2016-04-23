package util.ref.consts;

public class FloatConst implements Const {
    private final float value;

    private FloatConst(float value) {
        this.value = value;
    }

    public static FloatConst of(float value) {
        return new FloatConst(value);
    }

    public float getValue() {
        return value;
    }
}

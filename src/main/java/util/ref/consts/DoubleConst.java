package util.ref.consts;

public class DoubleConst implements Const {
    private final double value;

    private DoubleConst(double value) {
        this.value = value;
    }

    public static DoubleConst of(double value) {
        return new DoubleConst(value);
    }

    public double getValue() {
        return value;
    }
}

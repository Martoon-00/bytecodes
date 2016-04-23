package util.ref.consts;

public class StringConst implements Const {
    private final String value;

    private StringConst(String value) {
        this.value = value;
    }

    public static StringConst of(String value) {
        return new StringConst(value);
    }

    public String getValue() {
        return value;
    }
}

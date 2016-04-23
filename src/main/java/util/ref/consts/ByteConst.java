package util.ref.consts;

public class ByteConst implements Const {
    private final byte value;

    public ByteConst(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}

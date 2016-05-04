package util.op;

public enum BinOpType {
    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/"),
    REM("%"),
    SHIFT_LEFT("<<"),
    SHIFT_RIGHT_ARITHMETIC(">>"),
    SHIFT_RIGHT_LOGIC(">>>"),
    AND("&"),
    OR("|"),
    XOR("^");

    private final String sym;

    BinOpType(String sym) {
        this.sym = sym;
    }

    public String getSym() {
        return sym;
    }
}

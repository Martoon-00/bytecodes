package util.op;

public enum UnaryOpType {
    NEG("-");

    private final String sym;

     UnaryOpType(String sym) {
        this.sym = sym;
    }

    public String getSym() {
        return sym;
    }
}

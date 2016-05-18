package scan.except;

public class UnknownConstSort extends RuntimeException {
    private final Object cst;
    private final int sort;

    public UnknownConstSort(Object cst, int sort) {
        super(String.format("Unknown sort type %d of constant %s", sort, cst));
        this.cst = cst;
        this.sort = sort;
    }

    public int getSort() {
        return sort;
    }
}

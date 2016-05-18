package scan.except;

public class UnknownConstType extends RuntimeException{
    private final Object object;

    public UnknownConstType(Object object) {
        super("Unknown const type: " + object);
        this.object = object;
    }

    public Object getConst() {
        return object;
    }
}

package util.ref;

@FunctionalInterface
public interface BinOp {
    Object apply(Object a, Object b);
}

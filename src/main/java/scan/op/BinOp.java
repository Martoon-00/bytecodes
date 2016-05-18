package scan.op;

@FunctionalInterface
public interface BinOp {
    Object apply(Object a, Object b);
}

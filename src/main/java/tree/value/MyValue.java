package tree.value;

import inter.InterContext;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.BasicValue;
import scan.except.InvalidBytecodeException;

import java.util.HashMap;
import java.util.Map;

public abstract class MyValue extends BasicValue {
    public MyValue(Type type) {
        super(type);
    }

    public int getSize() {
        Type type = getType();
        return type == Type.LONG_TYPE || type == Type.DOUBLE_TYPE ? 2 : 1;
    }

    public boolean isReference() {
        Type type = getType();
        return type != null
                && (type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY);
    }

    /**
     * Whether it is recursive value and nether can be fully counted
     *
     * @param visited for each node above this in tree - whether is there a complicating node between it and this
     *                (unary or binary operation)
     * @return value without recursion
     */
    protected abstract MyValue proceedElimRec(Map<MyValue, Boolean> visited);

    public final MyValue eliminateRecursion(Map<MyValue, Boolean> visited) {
        boolean exists = visited.containsKey(this);
        if (exists) {
            return visited.get(this) ? AnyValue.of(getType()) : new NoValue();
        } else {
            visited.put(this, false);
            int size = visited.size();
            MyValue res = proceedElimRec(visited);
            if (visited.size() != size)
                throw new Error();
            visited.remove(this);
            return res;
        }
    }

    public final MyValue eliminateRecursion() {
        return eliminateRecursion(new HashMap<>());
    }

    public abstract MyValue simplify();

    public static void assertSameType(MyValue v1, MyValue v2) {
        Type t1 = v1.getType();
        Type t2 = v2.getType();
        if (t1 == null || t2 == null)
            return;
        // for String == Object
//        if (t1.toString().endsWith(";") && t2.toString().endsWith(";"))
//            return;
        if (!t1.equals(t2))
            throw new InvalidBytecodeException(String.format("Alternatives with different types: %s vs %s", t1, t2));
    }

    public abstract MyValue resolveReferences(InterContext context, int depth);

    public abstract MyValue eliminateReferences();

    public abstract MyValue copy();

    @Override
    public boolean equals(Object value) {
        return this == value;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

}

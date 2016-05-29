package tree.value;

import intra.IntraContext;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.BasicValue;
import scan.except.InvalidBytecodeException;

import java.util.HashSet;
import java.util.Set;

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
     * @param visited     set of visited objects
     * @param complicated whether values beneath are wrapped.
     *                    Needed for case V1 = V1 | V2, V2 = V2 | V1 (answer)
     * @return value without recursion
     */
    protected abstract MyValue proceedElimRec(Set<MyValue> visited, boolean complicated);

    public final MyValue eliminateRecursion(Set<MyValue> visited, boolean complicated) {
        boolean added = visited.add(this);
        if (!added) {
            return complicated ? new AnyValue(getType()) : new NoValue();
        } else {
            MyValue res = proceedElimRec(visited, complicated);
            visited.remove(this);
            return res;
        }
    }

    public final MyValue eliminateRecursion() {
        return eliminateRecursion(new HashSet<>(), false);
    }

    public abstract MyValue simplify();

    public static void assertSameType(MyValue v1, MyValue v2) {
        Type t1 = v1.getType();
        Type t2 = v2.getType();
        if (t1 == null || t2 == null)
            return;
        if (t1.toString().endsWith(";") && t2.toString().endsWith(";"))
            return;
        if (!t1.equals(t2))
            throw new InvalidBytecodeException(String.format("Alternatives with different types: %s vs %s", t1, t2));
    }

    public abstract MyValue resolveReferences(IntraContext context, int depth);

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

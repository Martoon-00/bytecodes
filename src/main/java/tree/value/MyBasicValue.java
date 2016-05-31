package tree.value;

import intra.IntraContext;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.BasicValue;

import java.util.Set;

public class MyBasicValue extends MyValue {
    private MyBasicValue(Type type) {
        super(type);
    }

//    public static MyBasicValue of(BasicValue value) {
//        if (value == null)
//            return null;
//        if (value instanceof MyValue)
//            throw new IllegalStateException("Alolol");
//        return new MyBasicValue(value.getType());
//    }

    @Override
    protected MyValue proceedElimRec(Set<MyValue> visited, boolean complicated) {
//        if (value instanceof MyValue) {
//            return ((MyValue) value).eliminateRecursion(visited, complicated);
//        } else {
            return this;
//        }
    }

    @Override
    public MyValue simplify() {
        return this;
    }

    @Override
    public MyValue resolveReferences(IntraContext context, int depth) {
        return this;
    }

    @Override
    public MyValue eliminateReferences() {
        return this;
    }

    @Override
    public MyValue copy() {
        return new MyBasicValue(getType());
    }

    @Override
    public String toString() {
        return new BasicValue(getType()).toString();
    }
}

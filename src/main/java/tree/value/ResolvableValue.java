package tree.value;

import com.sun.istack.internal.NotNull;
import inter.InterContext;
import org.objectweb.asm.Type;

import java.util.Set;

public abstract class ResolvableValue extends LinkValue {
    protected boolean resolved;

    protected ResolvableValue(@NotNull Type type) {
        super(AnyValue.of(type));
    }

    @Override
    protected MyValue proceedElimRec(Set<MyValue> visited, boolean complicated) {
        return resolved ? super.proceedElimRec(visited, complicated) : this;
    }

    @Override
    public final MyValue resolveReferences(InterContext context, int depth) {
        if (!resolved) {  // if already resolved, we can be in recursion
            resolved = true;
            MyValue values = resolveUnresolved(context, depth);
            replaceEntry(was -> LinkValue.of(values));
        }
        return this;
    }

    protected abstract MyValue resolveUnresolved(InterContext context, int depth);

    @Override
    public MyValue copy() {
        if (resolved)
            throw new IllegalStateException("Already resolved");
        return copyUnresolved();
    }

    protected abstract MyValue copyUnresolved();

    @Override
    public MyValue simplify() {
        return resolved ? super.simplify() : this;
    }

    @Override
    public MyValue eliminateReferences() {
        return resolved ? getValue().eliminateReferences() : AnyValue.of(getType());
    }

}

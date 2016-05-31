package tree.value;

import intra.IntraContext;
import org.objectweb.asm.Type;

import java.util.Objects;

public class FieldRef extends ResolvableValue {
    private final String owner;
    private final String name;

    private FieldRef(String owner, String name, Type type) {
        super(type);
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);
        this.owner = owner;
        this.name = name;
    }

    public static FieldRef of(String clazz, String name, Type type) {
        return new FieldRef(clazz, name, type);
    }

    public String getName() {
        return name;
    }

    @Override
    protected MyValue resolveUnresolved(IntraContext context, int depth) {
        return context.getFieldValues(this);
    }

    @Override
    protected MyValue copyUnresolved() {
        return new FieldRef(owner, name, getType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldRef fieldRef = (FieldRef) o;

        return owner.equals(fieldRef.owner) && name.equals(fieldRef.name) && getType().equals(fieldRef.getType());
    }

    @Override
    public int hashCode() {
        return 31 * owner.hashCode() + name.hashCode();
    }

    public String toString() {
        String maybeValue = resolved ? ": " + getValue().toString() : "";
        return owner + '#' + name + maybeValue;
    }

    //TODO: for simplify, check whether they are equal
}

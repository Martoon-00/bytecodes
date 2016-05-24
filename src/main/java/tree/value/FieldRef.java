package tree.value;

import org.objectweb.asm.Type;

import java.util.Objects;

public class FieldRef extends FinalValue {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldRef fieldRef = (FieldRef) o;

        return owner.equals(fieldRef.owner) && name.equals(fieldRef.name);
    }

    @Override
    public int hashCode() {
        return 31 * owner.hashCode() + name.hashCode();
    }

    public String toString() {
        return "FieldRef{ " + owner + '#' + name + " }";
    }

    //TODO: for simplify, check whether they are equal
}

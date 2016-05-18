package scan;

import scan.ref.Ref;

public class FieldRef extends Ref {
    private final String clazz;
    private final String name;

    private FieldRef(String clazz, String name, RefType type) {
        super(type);
        this.clazz = clazz;
        this.name = name;
    }

    public static FieldRef of(String clazz, String name, RefType type) {
        return new FieldRef(clazz, name, type);
    }

    public String getName() {
        return name;
    }

    @Override
    public String show() {
        return "FieldRef{ " + clazz + '#' + name + " }";
    }

    //TODO: for simplify, check whether they are equal
}
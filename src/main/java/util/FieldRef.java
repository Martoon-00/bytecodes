package util;

import util.ref.Ref;
import util.ref.SpecValue;

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

    public static class FieldVal implements SpecValue {
        private final Ref ref;

        public FieldVal(Ref ref) {
            this.ref = ref;
        }
    }
}

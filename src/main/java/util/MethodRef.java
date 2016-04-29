package util;

public class MethodRef {
    private final String clazz;
    private final String name;

    private MethodRef(String clazz, String name) {
        this.clazz = clazz;
        this.name = name;
    }

    public static MethodRef of(String clazz, String name) {
        return new MethodRef(clazz, name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return clazz + "#" + name;
    }
}

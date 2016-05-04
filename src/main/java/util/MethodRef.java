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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodRef methodRef = (MethodRef) o;

        if (!clazz.equals(methodRef.clazz)) return false;
        return name.equals(methodRef.name);

    }

    @Override
    public int hashCode() {
        int result = clazz.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}

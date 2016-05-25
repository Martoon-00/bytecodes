package scan;

import javax.annotation.Nullable;

public class MethodRef {
    @Nullable
    private final String clazz;
    private final String name;
    private final String desc;

    private MethodRef(@Nullable String clazz, String name, String desc) {
        this.clazz = clazz;
        this.name = name;
        this.desc = desc;
    }

    public static MethodRef of(String clazz, String name, String desc) {
        return new MethodRef(clazz, name, desc);
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getClazz() {
        return clazz;
    }

    public String getDesc() {
        return desc;
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
        if (!name.equals(methodRef.name)) return false;
        return desc.equals(methodRef.desc);

    }

    @Override
    public int hashCode() {
        int result = clazz.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + desc.hashCode();
        return result;
    }
}

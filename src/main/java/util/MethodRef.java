package util;

public class MethodRef {
    private final String name;

    private MethodRef(String name) {
        this.name = name;
    }

    public static MethodRef of(String name) {
        // make it caching
        return new MethodRef(name);
    }

    public String getName() {
        return name;
    }
}

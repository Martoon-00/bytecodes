package util.ref;

public class Any implements Ref {
    private Any() {
    }

    public static Ref val() {
        return new Any();
    }
}

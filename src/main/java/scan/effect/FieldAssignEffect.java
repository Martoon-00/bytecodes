package scan.effect;

import scan.FieldRef;
import scan.MethodRef;
import scan.ref.Ref;

public class FieldAssignEffect implements Effect {
    private final MethodRef caller;
    private final FieldRef field;
    private final Ref value;

    private FieldAssignEffect(MethodRef caller, FieldRef field, Ref value) {
        this.caller = caller;
        this.field = field;
        this.value = value;
    }

    public static FieldAssignEffect of(MethodRef caller, FieldRef field, Ref value) {
        return new FieldAssignEffect(caller, field, value);
    }

    @Override
    public String toString() {
        return "FieldAssignEffect{ " + field + " <- " + value + " }";
    }

    public MethodRef getCaller() {
        return caller;
    }

    public FieldRef getField() {
        return field;
    }

    public Ref getValue() {
        return value;
    }
}

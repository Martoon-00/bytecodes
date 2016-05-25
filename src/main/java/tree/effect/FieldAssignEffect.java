package tree.effect;

import scan.MethodRef;
import tree.value.FieldRef;
import tree.value.MyValue;

public class FieldAssignEffect {
    private final MethodRef caller;
    private final FieldRef field;
    private MyValue value;

    private FieldAssignEffect(MethodRef caller, FieldRef field, MyValue value) {
        this.caller = caller;
        this.field = field;
        this.value = value;
    }

    public static FieldAssignEffect of(MethodRef caller, FieldRef field, MyValue value) {
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

    public MyValue getValue() {
        return value;
    }

    public void simplify() {
        value = value.eliminateRecursion().simplify();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldAssignEffect that = (FieldAssignEffect) o;

        if (!caller.equals(that.caller)) return false;
        if (!field.equals(that.field)) return false;
        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        int result = caller.hashCode();
        result = 31 * result + field.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}

package util.effect;

import util.FieldRef;
import util.ref.Ref;

public class FieldAssignEffect implements Effect {
    private final FieldRef field;
    private final Ref value;

    private FieldAssignEffect(FieldRef field, Ref value) {
        this.field = field;
        this.value = value;
    }

    public static FieldAssignEffect of(FieldRef field, Ref value) {
        return new FieldAssignEffect(field, value);
    }
}

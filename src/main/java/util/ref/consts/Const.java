package util.ref.consts;

import util.ref.Ref;

public class Const implements Ref {
    private final Object value;

    public Const(Object value) {
        this.value = value;
    }
}

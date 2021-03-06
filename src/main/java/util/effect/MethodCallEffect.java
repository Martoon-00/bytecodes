package util.effect;

import util.ref.MethodRef;
import util.ref.Ref;

import java.util.List;

public class MethodCallEffect implements Effect {
    private final MethodRef method;
    private final List<Ref> params;

    public MethodCallEffect(MethodRef method, List<Ref> params) {
        this.method = method;
        this.params = params;
    }


}

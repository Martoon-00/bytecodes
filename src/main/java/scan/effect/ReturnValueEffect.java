package scan.effect;

import scan.ref.Ref;

public class ReturnValueEffect implements Effect {
    private final Ref result;

    public ReturnValueEffect(Ref result) {
        this.result = result;
    }
}

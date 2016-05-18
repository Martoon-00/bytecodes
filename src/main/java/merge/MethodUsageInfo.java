package merge;

import scan.effect.MethodCallEffect;
import scan.ref.Ref;

import java.util.List;

public class MethodUsageInfo {
    private final Ref returnValue;
    private final List<MethodCallEffect> calls;

    public MethodUsageInfo(Ref returnValue, List<MethodCallEffect> calls) {
        this.returnValue = returnValue;
        this.calls = calls;
    }

    public Ref getReturnValue() {
        return returnValue;
    }

    public List<MethodCallEffect> getCalls() {
        return calls;
    }
}

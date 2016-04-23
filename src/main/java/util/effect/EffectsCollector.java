package util.effect;

import util.ref.Ref;

import java.util.ArrayList;
import java.util.List;

public class EffectsCollector {
    private final List<Ref> returnValues = new ArrayList<>();
    private final List<MethodCallEffect> methodCalls = new ArrayList<>();
    private final List<FieldAssignEffect> fieldAssigns = new ArrayList<>();

    public void addReturnValue(Ref ref) {
        returnValues.add(ref);
    }

    public void addMethodCall(MethodCallEffect methodCall) {
        methodCalls.add(methodCall);
    }

    public void addFieldAssign(FieldAssignEffect fieldAssign) {
        fieldAssigns.add(fieldAssign);
    }

}

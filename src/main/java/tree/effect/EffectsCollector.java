package tree.effect;

import tree.value.AltValue;
import tree.value.MyValue;

import java.util.ArrayList;
import java.util.List;

public class EffectsCollector {
    private final List<MyValue> returnValues = new ArrayList<>();
    private final List<MethodCallEffect> methodCalls = new ArrayList<>();
    private final List<FieldAssignEffect> fieldAssigns = new ArrayList<>();

    public EffectsCollector() {
    }

    public void addReturnValue(MyValue ref) {
        returnValues.add(ref);
    }

    public void addMethodCall(MethodCallEffect methodCall) {
        methodCalls.add(methodCall);
    }

    public void addFieldAssign(FieldAssignEffect fieldAssign) {
        fieldAssigns.add(fieldAssign);
    }

    public EffectsView build() {
        MyValue returnValue = AltValue.of(returnValues.toArray(new MyValue[0]));

        returnValue = returnValue.simplify();
        methodCalls.forEach(MethodCallEffect::simplify);
        fieldAssigns.forEach(FieldAssignEffect::simplify);

        return new EffectsView(
                returnValue,
                MethodCallEffect.mergeMethodCalls(methodCalls),
//                methodCalls,
                fieldAssigns
        );
    }


}

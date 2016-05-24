package tree.effect;

import scan.MethodRef;
import tree.value.AltValue;
import tree.value.MyValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EffectsCollector {
    private final MethodRef caller;
    private final List<MyValue> returnValues = new ArrayList<>();
    private final List<MethodCallEffect> methodCalls = new ArrayList<>();
    private final List<FieldAssignEffect> fieldAssigns = new ArrayList<>();

    public EffectsCollector(MethodRef caller) {
        this.caller = caller;
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

        returnValue.simplify();
        methodCalls.forEach(MethodCallEffect::simplify);
        fieldAssigns.forEach(FieldAssignEffect::simplify);

        return new EffectsView(
                returnValue,
//                mergeMethodCalls(methodCalls),
                methodCalls,
                fieldAssigns
        );
    }

    private List<MethodCallEffect> mergeMethodCalls(List<MethodCallEffect> calls) {
        return calls.stream()
                .collect(Collectors.groupingBy(
                        MethodCallEffect::getCallee,
                        Collectors.toList()
                ))
                .entrySet().stream()
                .map(entry -> new MethodCallEffect(caller, entry.getKey(), mergeMethodCallsParams(entry.getValue())))
                .collect(Collectors.toList());
    }

    private static List<MyValue> mergeMethodCallsParams(List<MethodCallEffect> methodCalls) {
        if (methodCalls.size() == 0)
            throw new IllegalArgumentException();

        int paramNum = methodCalls.get(0).getParams().size();
        // TODO: bad, (1, 10) | (2, 10) | (1, 20)  ->  (1 | 2, 10 | 20)
        return Stream.iterate(0, i -> i + 1).limit(paramNum)
                .map(j -> AltValue.of(
                        methodCalls.stream()
                                .map(call -> call.getParams().get(j))
                                .toArray(MyValue[]::new)
                ))
                .collect(Collectors.toList());
    }
}

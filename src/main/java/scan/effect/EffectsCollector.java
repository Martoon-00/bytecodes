package scan.effect;

import scan.MethodRef;
import scan.ref.PackRef;
import scan.ref.Ref;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EffectsCollector {
    private final MethodRef caller;
    private final List<Ref> returnValues = new ArrayList<>();
    private final List<MethodCallEffect> methodCalls = new ArrayList<>();
    private final List<FieldAssignEffect> fieldAssigns = new ArrayList<>();

    public EffectsCollector(MethodRef caller) {
        this.caller = caller;
    }

    public void addReturnValue(Ref ref) {
        returnValues.add(ref);
    }

    public void addMethodCall(MethodCallEffect methodCall) {
        methodCalls.add(methodCall);
    }

    public void addFieldAssign(FieldAssignEffect fieldAssign) {
        fieldAssigns.add(fieldAssign);
    }

    public EffectsView build() {
        return new EffectsView(
                PackRef.of(returnValues.toArray(new Ref[0])),
                mergeMethodCalls(methodCalls),
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

    private static List<Ref> mergeMethodCallsParams(List<MethodCallEffect> methodCalls) {
        if (methodCalls.size() == 0)
            throw new IllegalArgumentException();

        int paramNum = methodCalls.get(0).getParams().size();
        return Stream.iterate(0, i -> i + 1).limit(paramNum)
                .map(j -> PackRef.of(
                        methodCalls.stream()
                                .map(call -> call.getParams().get(j))
                                .toArray(Ref[]::new)
                ))
                .collect(Collectors.toList());
    }
}

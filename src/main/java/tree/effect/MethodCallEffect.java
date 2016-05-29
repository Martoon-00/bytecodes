package tree.effect;

import scan.MethodRef;
import tree.value.AltValue;
import tree.value.MyValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodCallEffect {
    private final MethodRef caller;
    private final MethodRef callee;
    private final List<MyValue> params;

    public MethodCallEffect(MethodRef caller, MethodRef callee, List<MyValue> params) {
        this.caller = caller;
        this.callee = callee;
        this.params = params;
    }

    @Override
    public String toString() {
        return "MethodCallEffect{ " + caller + " -> " + callee + "( " + params + " ) } ";
    }

    public MethodRef getCallee() {
        return callee;
    }

    public List<MyValue> getParams() {
        return params;
    }

    public MethodRef getCaller() {
        return caller;
    }

    public void simplify() {
        for (int i = 0; i < params.size(); i++) {
            params.set(i, params.get(i).eliminateRecursion().simplify());
        }
    }

    public static List<MethodCallEffect> mergeMethodCalls(List<MethodCallEffect> calls) {
        if (calls.size() == 0)
            return new ArrayList<>();

        MethodRef caller = calls.get(0).getCaller();
        return calls.stream()
                .collect(Collectors.groupingBy(
                        MethodCallEffect::getCallee,
                        Collectors.toList()
                ))
                .entrySet().stream()
                .map(entry -> new MethodCallEffect(caller, entry.getKey(), mergeMethodCallsParams(entry.getValue())))
                .collect(Collectors.toList());
    }

    public static List<MyValue> mergeMethodCallsParams(List<MethodCallEffect> methodCalls) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodCallEffect that = (MethodCallEffect) o;

        if (!caller.equals(that.caller)) return false;
        if (!callee.equals(that.callee)) return false;
        return params.equals(that.params);

    }

    @Override
    public int hashCode() {
        int result = caller.hashCode();
        result = 31 * result + callee.hashCode();
        result = 31 * result + params.hashCode();
        return result;
    }
}

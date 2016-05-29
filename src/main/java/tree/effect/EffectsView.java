package tree.effect;

import tree.value.AltValue;
import tree.value.MyValue;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class EffectsView {
    private final MyValue returnValue;
    private final List<MethodCallEffect> methodCalls;
    private final List<FieldAssignEffect> fieldAssigns;

    public EffectsView(MyValue returnValue, List<MethodCallEffect> methodCalls, List<FieldAssignEffect> fieldAssigns) {
        this.returnValue = returnValue;
        this.methodCalls = methodCalls;
        this.fieldAssigns = fieldAssigns;
    }

    public MyValue getReturnValue() {
        return returnValue;
    }

    public List<MethodCallEffect> getMethodCalls() {
        return methodCalls;
    }

    public List<FieldAssignEffect> getFieldAssigns() {
        return fieldAssigns;
    }

    public static EffectsView merge(Collection<EffectsView> effects) {
        return new EffectsView(
                AltValue.of(effects.stream()
                        .map(EffectsView::getReturnValue)
                        .toArray(MyValue[]::new)),
                effects.stream()
                        .map(EffectsView::getMethodCalls)
                        .flatMap(List::stream)
                        .collect(Collectors.toList()),
                effects.stream()
                        .map(EffectsView::getFieldAssigns)
                        .flatMap(List::stream)
                        .collect(Collectors.toList())
        );
    }

    public void print() {
        System.out.println("Return value:");
        System.out.println("\t" + returnValue);
        System.out.println("Method calls:");
        for (MethodCallEffect methodCall : methodCalls) {
            System.out.println("\t" + methodCall);
        }
        System.out.println("Field assignments:");
        for (FieldAssignEffect fieldAssign : fieldAssigns) {
            System.out.println("\t" + fieldAssign);
        }
    }
}

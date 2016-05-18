package scan.effect;

import scan.ref.Ref;

import java.util.List;

public class EffectsView {
    private final Ref returnValue;
    private final List<MethodCallEffect> methodCalls;
    private final List<FieldAssignEffect> fieldAssigns;

    EffectsView(Ref returnValue, List<MethodCallEffect> methodCalls, List<FieldAssignEffect> fieldAssigns) {
        this.returnValue = returnValue;
        this.methodCalls = methodCalls;
        this.fieldAssigns = fieldAssigns;
    }

    public Ref getReturnValue() {
        return returnValue;
    }

    public List<MethodCallEffect> getMethodCalls() {
        return methodCalls;
    }

    public List<FieldAssignEffect> getFieldAssigns() {
        return fieldAssigns;
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

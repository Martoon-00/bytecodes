package merge;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import scan.FieldRef;
import scan.MethodRef;
import scan.effect.EffectsView;
import scan.effect.FieldAssignEffect;
import scan.effect.MethodCallEffect;

import java.io.PrintStream;

public class EffectsOverallKeeper {
    // TODO: multimap!
    private final Multimap<MethodRef, MethodCallEffect> usages = HashMultimap.create();
    private final Multimap<FieldRef, FieldAssignEffect> assigns = HashMultimap.create();

    public void addEffects(MethodRef caller, EffectsView effects) {
        for (MethodCallEffect methodCall : effects.getMethodCalls()) {
            usages.put(methodCall.getCallee(), methodCall);
        }
        for (FieldAssignEffect fieldAssign : effects.getFieldAssigns()) {
            assigns.put(fieldAssign.getField(), fieldAssign);
        }
    }

    public void print(PrintStream out) {
        out.println("Overall effects:");
        usages.asMap().forEach((callee, effects) -> {
            out.println("\tCalls of " + callee + ":");
            effects.forEach(effect -> out.println("\t\t" + effect));
        });
        out.println();
        out.println("Overall field assignments:");
        assigns.asMap().forEach((field, assigns) -> {
            out.println("\tAssigns of " + field + ":");
            assigns.forEach(assign -> out.println("\t\t" + assign.getField() + " <- " + assign.getValue()));
        });
    }
}

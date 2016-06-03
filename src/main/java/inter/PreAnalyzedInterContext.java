package inter;

import scan.MethodRef;
import tree.effect.EffectsView;
import tree.effect.FieldAssignEffect;
import tree.effect.MethodCallEffect;
import tree.value.AltValue;
import tree.value.FieldRef;
import tree.value.MyValue;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PreAnalyzedInterContext implements InterContext {
    private final Collection<EffectsView> effects;

    public PreAnalyzedInterContext(Collection<EffectsView> effects) {
        this.effects = effects;
    }

    @Override
    public List<MethodCallEffect> getCallEffects(MethodRef method) {
        return effects.stream()
                .map(EffectsView::getMethodCalls)
                .flatMap(List::stream)
                .filter(methodCallEffect -> methodCallEffect.getCallee().equals(method))
                .collect(Collectors.toList());
    }

    @Override
    public MyValue getFieldValues(FieldRef field) {
        return AltValue.of(effects.stream()
                .map(EffectsView::getFieldAssigns)
                .flatMap(List::stream)
                .filter(fieldAssignEffect -> fieldAssignEffect.getField().equals(field))
                .map(FieldAssignEffect::getValue)
                .toArray(MyValue[]::new));
    }
}

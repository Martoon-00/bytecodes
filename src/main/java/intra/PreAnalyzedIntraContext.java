package intra;

import scan.MethodRef;
import tree.effect.EffectsView;
import tree.effect.FieldAssignEffect;
import tree.effect.MethodCallEffect;
import tree.value.FieldRef;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PreAnalyzedIntraContext implements IntraContext {
    private final Collection<EffectsView> effects;

    public PreAnalyzedIntraContext(Collection<EffectsView> effects) {
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
    public List<FieldAssignEffect> getFieldAssignEffects(FieldRef field) {
        return effects.stream()
                .map(EffectsView::getFieldAssigns)
                .flatMap(List::stream)
                .filter(fieldAssignEffect -> fieldAssignEffect.getField().equals(field))
                .collect(Collectors.toList());
    }
}

package intra;

import scan.MethodRef;
import tree.effect.FieldAssignEffect;
import tree.effect.MethodCallEffect;
import tree.value.FieldRef;

import java.util.List;

public interface IntraContext {
    List<MethodCallEffect> getCallEffects(MethodRef methodRef);

    List<FieldAssignEffect> getFieldAssignEffects(FieldRef field);
}

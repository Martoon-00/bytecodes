package inter;

import scan.MethodRef;
import tree.effect.MethodCallEffect;
import tree.value.FieldRef;
import tree.value.MyValue;

import java.util.List;

public interface InterContext {
    List<MethodCallEffect> getCallEffects(MethodRef methodRef);

    MyValue getFieldValues(FieldRef field);
}

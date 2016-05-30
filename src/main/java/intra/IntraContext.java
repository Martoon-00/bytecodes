package intra;

import scan.MethodRef;
import tree.effect.MethodCallEffect;
import tree.value.FieldRef;
import tree.value.MyValue;

import java.util.List;

public interface IntraContext {
    List<MethodCallEffect> getCallEffects(MethodRef methodRef);

    MyValue getFieldValues(FieldRef field);
}

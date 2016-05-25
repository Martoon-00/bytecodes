package basic;

import org.objectweb.asm.Type;
import scan.MethodRef;
import tree.effect.EffectsView;
import tree.effect.FieldAssignEffect;
import tree.value.ConstValue;
import tree.value.NoValue;
import util.TestBase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FieldAssignTest extends TestBase {
    @Override
    public Map<MethodRef, EffectsView> answer() {
        return new HashMap<MethodRef, EffectsView>(){{
            MethodRef lolMethod = localMethod("lol(I)V");
            put(lolMethod, new EffectsView(
                    new NoValue(),
                    Collections.emptyList(),
                    Collections.singletonList(
                            FieldAssignEffect.of(lolMethod, localField("f", "D"), new ConstValue(Type.DOUBLE_TYPE, 3.0))
                    )
            ));
        }};
    }
}

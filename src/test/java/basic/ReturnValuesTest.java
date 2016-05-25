package basic;

import org.objectweb.asm.Type;
import scan.MethodRef;
import tree.effect.EffectsView;
import tree.value.MethodParamValue;
import util.TestBase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReturnValuesTest extends TestBase {
    @Override
    public Map<MethodRef, EffectsView> answer() {
        return new HashMap<MethodRef, EffectsView>(){{
            MethodRef lolMethod = localMethod("lol(I)I");
            put(lolMethod, new EffectsView(
                    new MethodParamValue(lolMethod, 1, Type.INT_TYPE),
                    Collections.emptyList(),
                    Collections.emptyList()
            ));
        }};
    }
}

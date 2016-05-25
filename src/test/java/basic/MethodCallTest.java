package basic;

import org.objectweb.asm.Type;
import scan.MethodRef;
import tree.effect.EffectsView;
import tree.effect.MethodCallEffect;
import tree.value.ConstValue;
import tree.value.NoValue;
import util.TestBase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MethodCallTest extends TestBase {
    @Override
    public Map<MethodRef, EffectsView> answer() {
        return new HashMap<MethodRef, EffectsView>(){{
            MethodRef lolMethod = localMethod("lol(I)V");
            put(lolMethod, new EffectsView(
                    new NoValue(),
                    Collections.singletonList(
                            new MethodCallEffect(lolMethod, localMethod("meme(I)V"),
                                    Collections.singletonList(new ConstValue(Type.INT_TYPE, 5)))
                    ),
                    Collections.emptyList()
            ));
        }};
    }
}

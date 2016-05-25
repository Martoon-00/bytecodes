package util;

import org.objectweb.asm.Type;
import scan.MethodRef;
import tree.ClassScanner;
import tree.effect.EffectsView;
import tree.effect.FieldAssignEffect;
import tree.effect.MethodCallEffect;
import tree.value.FieldRef;
import tree.value.MyValue;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public abstract class TestBase {
    @org.junit.Test
    public void test() throws Exception {
        String clazz = getClass().getName().replaceAll("[.]", "/");
        String testedFile = "test-launch/target/classes/" + clazz + ".class";
        System.out.println(testedFile);
        Map<MethodRef, EffectsView> analyzeResult;
        try (InputStream in = new FileInputStream(testedFile)) {
            analyzeResult = new ClassScanner(in, clazz).analyze();
        }
        answer().forEach((method, expectedEffects) -> {
            EffectsView gainedEffect = analyzeResult.get(method);
            if (gainedEffect == null)
                return;

            MyValue gainedRet = gainedEffect.getReturnValue();
            MyValue expectedRet = expectedEffects.getReturnValue();
            if (!gainedRet.equals(expectedRet))
                throw new AssertionError(String.format("Wrong return value, expected \n%s, \nbut gained \n%s", expectedRet, gainedRet));

            List<MethodCallEffect> gainedCalls = gainedEffect.getMethodCalls();
            List<MethodCallEffect> expectedCalls = expectedEffects.getMethodCalls();
            checkContains(gainedCalls, expectedCalls, "Gained \n%s, \nwhich is not expected. Expected items:\n%s");
            checkContains(expectedCalls, gainedCalls, "Expected \n%s, \nwhich is not in result. Result:\n%s");

            List<FieldAssignEffect> gainedAssigns = gainedEffect.getFieldAssigns();
            List<FieldAssignEffect> expectedAssigns = expectedEffects.getFieldAssigns();
            checkContains(gainedAssigns, expectedAssigns, "Gained \n%s, \nwhich is not expected. Expected items:\n%s");
            checkContains(expectedAssigns, gainedAssigns, "Expected \n%s, \nwhich is not in result. Result:\n%s");
        });

    }

    private <T> void checkContains(List<T> gainedCalls, List<T> expectedCalls, String errorMsg) {
        for (T gainedCall : gainedCalls) {
            if (!expectedCalls.contains(gainedCall))
                throw new AssertionError(String.format(errorMsg, gainedCall, expectedCalls));
        }
    }

    protected abstract Map<MethodRef, EffectsView> answer();

    protected MethodRef localMethod(String signature) {
        int i = signature.indexOf("(");
        String clazz = getClass().getCanonicalName().replaceAll("[.]", "/");
        return MethodRef.of(clazz, signature.substring(0, i), signature.substring(i));
    }

    protected FieldRef localField(String name, String type) {
        String clazz = getClass().getCanonicalName().replaceAll("[.]", "/");
        return FieldRef.of(clazz, name, Type.getObjectType(type));
    }
}

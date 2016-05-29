package intra;

import org.objectweb.asm.tree.analysis.AnalyzerException;
import scan.MethodRef;
import tree.ClassScanner;
import tree.effect.EffectsView;
import tree.effect.MethodCallEffect;
import tree.value.MethodParamValue;
import tree.value.MyValue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntraAnalyzer {
    private final IntraContext context;

    public static void main(String[] args) throws IOException, AnalyzerException {
        String clazz = "Clazz";
        String clazzFile = "test-launch/target/classes/" + clazz.replaceAll("[.]", "/") + ".class";
        Map<MethodRef, EffectsView> results;
        try (InputStream is = new FileInputStream(clazzFile)) {
            results = new ClassScanner(is, clazz).analyze();
        }

        ArrayList<MethodRef> methods = new ArrayList<>(results.keySet());
        PreAnalyzedIntraContext context = new PreAnalyzedIntraContext(results.values());
        IntraAnalyzer intraAnalyzer = new IntraAnalyzer(context);

        Optional<MethodRef> lol = methods.stream().filter(m -> m.getName().equals("lol")).findAny();
        if (!lol.isPresent())
            throw new RuntimeException("No lol method found");
        MethodRef lolMethod = lol.get();
        Optional<List<MyValue>> maybeResult = intraAnalyzer.analyzePassedParams(lolMethod, 3, true);
        if (maybeResult.isPresent()) {
            List<MyValue> result = maybeResult.get();
            System.out.println(String.format("Result: (total of %d)", result.size()));
            for (int i = 0; i < result.size(); i++) {
                MyValue value = result.get(i);
                System.out.println("Param #" + i + ": " + value);
            }
        } else {
            System.out.println("No usages found");
        }
    }

    public IntraAnalyzer(IntraContext context) {
        this.context = context;
    }

    public Optional<List<MyValue>> analyzePassedParams(MethodRef method, int depth, boolean remainReferences) {
//        Type[] argumentTypes = Type.getArgumentTypes(method.getDesc());
        // TODO: maybe find a better way to get parameters?
        Optional<List<MyValue>> maybeParams = context.getCallEffects(method).stream()
                .findAny()
                .map(MethodCallEffect::getParams);
        if (!maybeParams.isPresent())
            return Optional.empty();
        List<MyValue> params = maybeParams.get();

        return Stream.iterate(0, i -> i + 1).limit(params.size())
                .map(i -> new MethodParamValue(method, i, params.get(i).getType()))
                .map(param -> resolve(param, depth, remainReferences))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of));
    }

    private MyValue resolve(MyValue value, int depth, boolean remainReferences) {
        MyValue result = value
                .resolveReferences(context, depth)
                .eliminateRecursion();
        if (!remainReferences)
            result = result.eliminateReferences();
        return result.simplify();
    }
}

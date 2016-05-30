import intra.IntraAnalyzer;
import intra.PreAnalyzedIntraContext;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import scan.MethodRef;
import tree.ClassScanner;
import tree.effect.EffectsView;
import tree.value.MyValue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Sandbox {

    public static void main(String[] args) throws IOException, AnalyzerException {
        performAnalysis("Clazz", "lol", 3, false);
    }

    /**
     * This method analyses specified class in "test-launch" module, and finds parameters passed to specified method.
     *
     * @param scannedClass     class to scan
     * @param methodName       specifies witch method's parameters' values should be found
     * @param depth            how deep resolution of method parameters and field values should follow
     * @param remainReferences whether to remain references to method parameters and field values, or replace them with
     *                         "any value"
     * @throws IOException
     * @throws AnalyzerException
     */
    private static void performAnalysis(String scannedClass, String methodName, int depth, boolean remainReferences)
            throws IOException, AnalyzerException {
        String clazzFile = "test-launch/target/classes/" + scannedClass.replaceAll("[.]", "/") + ".class";
        Map<MethodRef, EffectsView> results;
        try (InputStream is = new FileInputStream(clazzFile)) {
            results = new ClassScanner(is, scannedClass).analyze();
        }

        ArrayList<MethodRef> methods = new ArrayList<>(results.keySet());
        PreAnalyzedIntraContext context = new PreAnalyzedIntraContext(results.values());
        IntraAnalyzer intraAnalyzer = new IntraAnalyzer(context);

        Optional<MethodRef> lol = methods.stream()
                .filter(m -> m.getName().equals(methodName))
                .findAny();
        if (!lol.isPresent())
            throw new RuntimeException("No " + methodName + " method defined");
        MethodRef searchedMethod = lol.get();

        Optional<List<MyValue>> maybeResult = intraAnalyzer.analyzePassedParams(searchedMethod, depth, remainReferences);
        if (!maybeResult.isPresent()) {
            System.out.println("No usages found");
            return;
        }
        List<MyValue> result = maybeResult.get();

        System.out.println(String.format("Result: (total of %d)", result.size()));
        for (int i = 0; i < result.size(); i++) {
            MyValue value = result.get(i);
            System.out.println("Param #" + i + ": " + value);
        }
    }
}

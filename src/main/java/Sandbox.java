import inter.InterAnalyzer;
import inter.PreAnalyzedInterContext;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import scan.MethodRef;
import tree.ClassScanner;
import tree.effect.EffectsView;
import tree.value.MyValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Sandbox {

    public static void main(String[] args) throws IOException, AnalyzerException {
        if (args.length <= 3) {
            System.out.println("Not enough arguments at command line");
            help();
            return;
        }

        String classPath = args[0];
        String clazz = args[1];
        String methodName = args[2];
        int depth = Integer.parseInt(args[3]);
        boolean remainReferences = args.length > 4 && Boolean.parseBoolean(args[4]);

        performAnalysis(classPath, clazz, methodName, depth, remainReferences);

//        performAnalysis("test-launch/target/classes", "Clazz", "lol", 3, false);
    }

    private static void performAnalysis(String classPath, String clazz, String methodName, int depth, boolean remainReferences)
            throws IOException, AnalyzerException {
        String clazzFile = new File(classPath, clazz.replaceAll("[.]", "/") + ".class").getAbsolutePath();
        Map<MethodRef, EffectsView> results;
        try (InputStream is = new FileInputStream(clazzFile)) {
            results = new ClassScanner(is, clazz.replaceAll("[.]", "/")).analyze();
        }

        ArrayList<MethodRef> methods = new ArrayList<>(results.keySet());
        PreAnalyzedInterContext context = new PreAnalyzedInterContext(results.values());
        InterAnalyzer interAnalyzer = new InterAnalyzer(context);

        Optional<MethodRef> lol = methods.stream()
                .filter(m -> m.getName().equals(methodName))
                .findAny();
        if (!lol.isPresent())
            throw new RuntimeException("No " + methodName + " method defined");
        MethodRef searchedMethod = lol.get();

        Optional<List<MyValue>> maybeResult = interAnalyzer.analyzePassedParams(searchedMethod, depth, remainReferences);
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

    private static void help() {
        System.out.println("Expected parameters");
        System.out.println("1. Path to package with specified class");
        System.out.println("2. Fully classified class name");
        System.out.println("3. Method name to which parameters values should be found");
        System.out.println("4. Depth of scanning (how deep reference resolution should follow)");
        System.out.println("5. [optional] Whether to remain references to fields and method parameters (default - \"false\")");
        System.out.println("Example of usage: java Sandbox.class target/classes Clazz myMethod 3 false");
    }
}

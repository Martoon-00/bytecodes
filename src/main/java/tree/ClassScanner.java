package tree;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import scan.Cache;
import scan.MethodRef;
import tree.effect.EffectsView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassScanner {
    private final Cache cache = new Cache();

    private final String clazz;
    private final ClassReader cr;

    public ClassScanner(ClassReader cr, String clazz) {
        this.cr = cr;
        this.clazz = clazz;
    }

    public ClassScanner(String clazz) throws IOException {
        this(new ClassReader(clazz), clazz);
    }

    public ClassScanner(InputStream is, String clazz) throws IOException {
        this(new ClassReader(is), clazz);
    }

    public static void main(String[] args) throws IOException, AnalyzerException {
        String clazz = "Clazz";
        String clazzFile = "test-launch/target/classes/" + clazz.replaceAll("[.]", "/") + ".class";
        Map<MethodRef, EffectsView> results;
        try (InputStream is = new FileInputStream(clazzFile)) {
            results = new ClassScanner(is, clazz).analyze();
        }

        results.forEach((methodRef, effectsView) -> {
            System.out.println("Effects for " + methodRef + ":");
            effectsView.print();
            System.out.println();
        });
    }

    public Map<MethodRef, EffectsView> analyze() throws IOException, AnalyzerException {
        Scanner scanner = new Scanner();
        cr.accept(scanner, 0);

        HashMap<MethodRef, EffectsView> result = new HashMap<>();
        for (MethodNode methodNode : scanner.methodNodes) {
            MethodRef method = MethodRef.of(clazz, methodNode.name, methodNode.desc);
            ControlFlow analyzer = new ControlFlow(new LolInterpreter(method));
            analyzer.analyze(clazz, methodNode);
            result.put(method, analyzer.getEffects());
        }
        return result;
    }

    private class Scanner extends ClassVisitor {
        private List<MethodNode> methodNodes = new ArrayList<>();

        public Scanner() {
            super(Opcodes.ASM4);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodNode methodNode = new MethodNode(Opcodes.ASM4, access, name, desc, signature, exceptions);
            methodNodes.add(methodNode);
            return methodNode;
        }
    }
}

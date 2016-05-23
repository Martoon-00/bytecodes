import org.objectweb.asm.*;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.*;
import scan.Cache;
import scan.MethodRef;
import scan.RefType;
import scan.ref.MethodParamRef;
import scan.ref.Ref;
import scan.ref.ThisRef;
import tree.ControlFlow;
import scan.scanners.MethodScanner;
import tree.value.ReplaceableValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner extends ClassVisitor {
    private final Cache cache = new Cache();

    private final String clazz;

    public static List<MethodNode> methodNodes = new ArrayList<>();

    private final List<MethodScanner> methodScanners = new ArrayList<>();

    public ClassScanner(String clazz) {
        super(Opcodes.ASM4);
        this.clazz = clazz;
    }


    public static void main(String[] args) throws IOException, AnalyzerException {
        String clazz = "Clazz";
        ClassScanner cn = new ClassScanner(clazz);
        new ClassReader(clazz)
                .accept(cn, 0);

//        EffectsOverallKeeper effects = new EffectsOverallKeeper();
//        cn.methodScanners.forEach(scanner -> effects.addEffects(scanner.getMethod(), scanner.getEffects()));
//
//        effects.print(System.out);

//        for (MethodScanner methodScanner : cn.methodScanners) {
//            System.out.println("Analysis result for " + methodScanner.getMethod());
//            methodScanner.getEffects()
//                    .print();
//            System.out.println();
//            System.out.println();
//        }


        for (MethodNode methodNode : methodNodes) {
            ControlFlow cf = new ControlFlow();
            Frame<ReplaceableValue>[] res = cf.analyze(clazz, methodNode);
            res[20].getLocal(2).eliminateRecursion();
            double a = 2;
        }
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodNode methodNode = new MethodNode(Opcodes.ASM4, access, name, desc, signature, exceptions);
        methodNodes.add(methodNode);
        return methodNode;
    }

    //    @Override
    public MethodVisitor visitMethod0(int access, String name, String desc, String signature, String[] exceptions) {
        MethodRef methodRef = MethodRef.of(clazz, name, desc);

        ArrayList<Ref> params = new ArrayList<>();
        if ((access & Opcodes.ACC_STATIC) == 0)
            params.add(ThisRef.val());

        Type methodType = Type.getMethodType(desc);
        Type[] argumentTypes = methodType.getArgumentTypes();
        for (int i = 0; i < argumentTypes.length; i++) {
            Type argType = argumentTypes[i];
            params.add(new MethodParamRef(methodRef, i, RefType.fromAsmType(argType)));
        }

        MethodScanner methodScanner = new MethodScanner(methodRef, params, cache);
        methodScanners.add(methodScanner);
        return methodScanner;
    }
}

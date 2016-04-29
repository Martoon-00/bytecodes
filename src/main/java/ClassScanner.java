import org.objectweb.asm.*;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.*;
import util.MethodRef;
import util.RefType;
import util.ref.MethodParamRef;
import util.ref.Ref;
import util.ref.ThisRef;

import java.io.IOException;
import java.util.ArrayList;

public class ClassScanner extends ClassVisitor {
    private final String clazz;

    public static MethodNode methodNode;

    public ClassScanner(String clazz) {
        super(Opcodes.ASM4);
        this.clazz = clazz;
    }


    public static void main(String[] args) throws IOException {
        String clazz = "Clazz";
        new ClassReader(clazz)
                .accept(new ClassScanner(clazz), 0);

        Analyzer<BasicValue> a = new Analyzer<>(new LolInterpreter());
        try {
            a.analyze(clazz, methodNode);
            Frame<BasicValue>[] frames = a.getFrames();
            int k = 5;
        } catch (AnalyzerException e) {
            e.printStackTrace();
        }
    }

    public MethodVisitor visitMethod0(int access, String name, String desc, String signature, String[] exceptions) {
        return methodNode = new MethodNode(Opcodes.ASM4, access, name, desc, signature, exceptions);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodRef methodRef = MethodRef.of(clazz, name);

        ArrayList<Ref> params = new ArrayList<>();
        if ((access & Opcodes.ACC_STATIC) == 0)
            params.add(ThisRef.val());

        Type methodType = Type.getMethodType(desc);
        Type[] argumentTypes = methodType.getArgumentTypes();
        for (int i = 0; i < argumentTypes.length; i++) {
            Type argType = argumentTypes[i];
            params.add(new MethodParamRef(methodRef, i, RefType.fromAsmType(argType)));
        }

        return new MethodScanner(methodRef, params);
    }
}

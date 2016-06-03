package tree.value;

import inter.InterContext;
import org.objectweb.asm.Type;

public class ObjValue extends AnyValue {
    private final String clazz;

    protected ObjValue(String clazz) {
        super(Type.getObjectType("java/lang/Object"));
        this.clazz = clazz;
    }

    public static ObjValue of(String clazz) {
        return new ObjValue(clazz);
    }

    public String getClazz() {
        return clazz;
    }

    @Override
    public MyValue resolveReferences(InterContext context, int depth) {
        return this;
    }

    @Override
    public MyValue eliminateReferences() {
        return this;
    }

    @Override
    public String toString() {
        return "ObjValue{ " + clazz + " }";
    }
}

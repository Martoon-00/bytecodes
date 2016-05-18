import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.BasicValue;

import java.util.List;

public class LolInterpreter extends BasicInterpreter {
    protected LolInterpreter() {
        super(Opcodes.ASM4);
    }

    @Override
    public BasicValue newValue(Type type) {
        return super.newValue(type);
    }

    @Override
    public BasicValue newOperation(AbstractInsnNode insn) throws AnalyzerException {
        switch (insn.getOpcode()) {
            case Opcodes.ACONST_NULL:
                return new LolValue(Type.getObjectType("java/lang/Object"), null);
            case Opcodes.ICONST_M1:
                return new LolValue(Type.INT_TYPE, -1);
            case Opcodes.ICONST_0:
                return new LolValue(Type.INT_TYPE, 0);
            case Opcodes.ICONST_1:
                return new LolValue(Type.INT_TYPE, 1);
            case Opcodes.ICONST_2:
                return new LolValue(Type.INT_TYPE, 2);
            case Opcodes.ICONST_3:
                return new LolValue(Type.INT_TYPE, 3);
            case Opcodes.ICONST_4:
                return new LolValue(Type.INT_TYPE, 4);
            case Opcodes.ICONST_5:
                return new LolValue(Type.INT_TYPE, 5);
            case Opcodes.LCONST_0:
                return new LolValue(Type.LONG_TYPE, 0);
            case Opcodes.LCONST_1:
                return new LolValue(Type.LONG_TYPE, 1);
            case Opcodes.FCONST_0:
                return new LolValue(Type.FLOAT_TYPE, 0);
            case Opcodes.FCONST_1:
                return new LolValue(Type.LONG_TYPE, 1);
            case Opcodes.FCONST_2:
                return new LolValue(Type.LONG_TYPE, 2);
            case Opcodes.DCONST_0:
                return new LolValue(Type.DOUBLE_TYPE, 0);
            case Opcodes.DCONST_1:
                return new LolValue(Type.DOUBLE_TYPE, 1);
        }
        return super.newOperation(insn);
    }

    @Override
    public BasicValue copyOperation(AbstractInsnNode insn, BasicValue value) throws AnalyzerException {
        return super.copyOperation(insn, value);
    }

    @Override
    public BasicValue unaryOperation(AbstractInsnNode insn, BasicValue value) throws AnalyzerException {
        return super.unaryOperation(insn, value);
    }

    @Override
    public BasicValue binaryOperation(AbstractInsnNode insn, BasicValue value1, BasicValue value2) throws AnalyzerException {
        return super.binaryOperation(insn, value1, value2);
    }

    @Override
    public BasicValue ternaryOperation(AbstractInsnNode insn, BasicValue value1, BasicValue value2, BasicValue value3) throws AnalyzerException {
        return super.ternaryOperation(insn, value1, value2, value3);
    }

    @Override
    public BasicValue naryOperation(AbstractInsnNode insn, List<? extends BasicValue> values) throws AnalyzerException {
        return super.naryOperation(insn, values);
    }

    @Override
    public void returnOperation(AbstractInsnNode insn, BasicValue value, BasicValue expected) throws AnalyzerException {
        super.returnOperation(insn, value, expected);
    }

    @Override
    public BasicValue merge(BasicValue v, BasicValue w) {
        return super.merge(v, w);
    }

    private static class LolValue extends BasicValue {
        private final Object value;

        public LolValue(Type type, Object value) {
            super(type);
            this.value = value;
        }

        @Override
        public String toString() {
            return "LolValue{ " + value + " }";
        }
    }

}

package tree;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Interpreter;
import scan.except.UnsupportedOpcodeException;
import tree.value.*;
import tree.value.op.BinOpValue;

import java.util.List;

public class LolInterpreter extends Interpreter<ReplaceableValue> {

    private final BasicInterpreter interpreter = new BasicInterpreter();

    private ConstValue constValue = new ConstValue(null, null);

    protected LolInterpreter() {
        super(Opcodes.ASM4);
    }

    @Override
    public ReplaceableValue newValue(Type type) {
        BasicValue v = interpreter.newValue(type);
        return ReplaceableValue.of(MyBasicValue.of(v));
    }

    @Override
    public ReplaceableValue newOperation(AbstractInsnNode insn) throws AnalyzerException {
        MyValue answer;
        switch (insn.getOpcode()) {
            case Opcodes.ACONST_NULL:
                answer = new NullValue(Type.getObjectType("java/lang/Object"));
                break;
            case Opcodes.ICONST_M1:
                answer = new ConstValue(Type.INT_TYPE, -1);
                break;
            case Opcodes.ICONST_0:
                answer = new ConstValue(Type.INT_TYPE, 0);
                break;
            case Opcodes.ICONST_1:
                answer = new ConstValue(Type.INT_TYPE, 1);
                break;
            case Opcodes.ICONST_2:
                answer = new ConstValue(Type.INT_TYPE, 2);
                break;
            case Opcodes.ICONST_3:
                answer = new ConstValue(Type.INT_TYPE, 3);
                break;
            case Opcodes.ICONST_4:
                answer = new ConstValue(Type.INT_TYPE, 4);
                break;
            case Opcodes.ICONST_5:
                answer = new ConstValue(Type.INT_TYPE, 5);
                break;
            case Opcodes.LCONST_0:
                answer = new ConstValue(Type.LONG_TYPE, 0L);
                break;
            case Opcodes.LCONST_1:
                answer = new ConstValue(Type.LONG_TYPE, 1L);
                break;
            case Opcodes.FCONST_0:
                answer = new ConstValue(Type.FLOAT_TYPE, 0F);
                break;
            case Opcodes.FCONST_1:
                answer = new ConstValue(Type.FLOAT_TYPE, 1F);
                break;
            case Opcodes.FCONST_2:
                answer = new ConstValue(Type.FLOAT_TYPE, 2F);
                break;
            case Opcodes.DCONST_0:
                answer = new ConstValue(Type.DOUBLE_TYPE, 0.0);
                break;
            case Opcodes.DCONST_1:
                answer = new ConstValue(Type.DOUBLE_TYPE, 1.0);
                break;
            case Opcodes.BIPUSH:
                answer = new ConstValue(Type.BYTE_TYPE, ((IntInsnNode) insn).operand);
                break;
            case Opcodes.SIPUSH:
                answer = new ConstValue(Type.SHORT_TYPE, ((IntInsnNode) insn).operand);
                break;
            case Opcodes.LDC:
                Object cst = ((LdcInsnNode) insn).cst;
                if (cst instanceof Integer) {
                    answer = new ConstValue(Type.INT_TYPE, cst);
                } else if (cst instanceof Float) {
                    answer = new ConstValue(Type.FLOAT_TYPE, cst);
                } else if (cst instanceof Long) {
                    answer = new ConstValue(Type.LONG_TYPE, cst);
                } else if (cst instanceof Double) {
                    answer = new ConstValue(Type.DOUBLE_TYPE, cst);
                } else if (cst instanceof String) {
                    answer = new ConstValue(Type.getObjectType("java/lang/String"), cst);
                } else if (cst instanceof Type) {
                    int sort = ((Type) cst).getSort();
                    if (sort == Type.OBJECT || sort == Type.ARRAY) {
                        answer = new AnyValue(Type.getObjectType("java/lang/Class"));
                    } else if (sort == Type.METHOD) {
                        answer = new AnyValue(Type.getObjectType("java/lang/invoke/MethodType"));
                    } else {
                        throw new IllegalArgumentException("Illegal LDC constant " + cst);
                    }
                } else if (cst instanceof Handle) {
                    answer = new AnyValue(Type.getObjectType("java/lang/invoke/MethodHandle"));
                } else {
                    throw new IllegalArgumentException("Illegal LDC constant " + cst);
                }
                break;
            case Opcodes.JSR:
                answer = MyBasicValue.of(BasicValue.RETURNADDRESS_VALUE);
                break;
            case Opcodes.GETSTATIC:
                answer = new ConstValue(Type.BYTE_TYPE, "Field: " + ((FieldInsnNode) insn).desc);
                break;
            case Opcodes.NEW:
                answer = new ConstValue(Type.BYTE_TYPE, "New: " + ((TypeInsnNode) insn).desc);
                break;
            default:
                throw new UnsupportedOpcodeException(insn.getOpcode());
        }
        return ReplaceableValue.of(answer);
    }

    @Override
    public ReplaceableValue copyOperation(AbstractInsnNode insn, ReplaceableValue value) throws AnalyzerException {
        return ReplaceableValue.of(MyBasicValue.of(interpreter.copyOperation(insn, value)));
    }

    @Override
    public ReplaceableValue unaryOperation(AbstractInsnNode insn, ReplaceableValue value) throws AnalyzerException {
        return ReplaceableValue.of(MyBasicValue.of(interpreter.unaryOperation(insn, value)));
    }

    @Override
    public ReplaceableValue binaryOperation(AbstractInsnNode insn, ReplaceableValue value1, ReplaceableValue value2) throws AnalyzerException {
        int opcode = insn.getOpcode();
        MyValue res;
        if (opcode == Opcodes.PUTFIELD) {
            return null;
        } else if (opcode >= 46 && opcode < 54) {  // load from array
            res = new AnyValue(interpreter.binaryOperation(insn, value1, value2).getType());
        } else {
            res = BinOpValue.of(opcode, value1, value2);
        }
        return ReplaceableValue.of(res);
    }

    @Override
    public ReplaceableValue ternaryOperation(AbstractInsnNode insn, ReplaceableValue value1, ReplaceableValue value2, ReplaceableValue value3) throws AnalyzerException {
        return ReplaceableValue.of(MyBasicValue.of(interpreter.ternaryOperation(insn, value1, value2, value3)));
    }

    @Override
    public ReplaceableValue naryOperation(AbstractInsnNode insn, List<? extends ReplaceableValue> values) throws AnalyzerException {
        return ReplaceableValue.of(MyBasicValue.of(interpreter.naryOperation(insn, values)));
    }

    @Override
    public void returnOperation(AbstractInsnNode insn, ReplaceableValue value, ReplaceableValue expected) throws AnalyzerException {
        interpreter.returnOperation(insn, value, expected);
    }

    @Override
    public ReplaceableValue merge(ReplaceableValue v, ReplaceableValue w) {
        throw new UnsupportedOperationException();
//        if (!v.getType().equals(w.getType()))
//            throw new InvalidBytecodeException("Different types of merged values");
//        Type type = v.getType();
//        return AltValue.of(type, v, w);
    }


}

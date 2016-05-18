package scan.noop;


import scan.Cache;
import scan.frame.Frame;
import scan.ref.Ref;
import scan.ref.op.BinOpRef;
import scan.ref.op.UnaryOpRef;

class NumOpInst {
//    private static final RefType[] types = {RefType.INT, RefType.LONG};
//    private static final RefType[] typesExt = {RefType.INT, RefType.LONG, RefType.FLOAT, RefType.DOUBLE};
//    private static final BinOpType[] arithmeticTypes = {BinOpType.ADD, BinOpType.SUB, BinOpType.MUL, BinOpType.DIV,
//            BinOpType.REM};
//    private static final BinOpType[] shiftTypes = {BinOpType.SHIFT_LEFT, BinOpType.SHIFT_RIGHT_ARITHMETIC,
//            BinOpType.SHIFT_RIGHT_LOGIC};
//    private static final BinOpType[] logicTypes = {BinOpType.AND, BinOpType.OR, BinOpType.XOR};

    private final Cache cache;
    private final Frame frame;

    public NumOpInst(Cache cache, Frame frame) {
        this.cache = cache;
        this.frame = frame;
    }

    public void apply(int opcode) {
        //  96 - 115:  (+, -, *, /, %) x (int, long, float, double)
        // 116 - 119:  (unary -)       x (int, long, float, double)
        // 120 - 125:  (<<, >>, >>>)   x (int, long)
        // 126 - 131:  (&, |, ^)       x (int, long)
        if (opcode >= 116 && opcode < 120) {
            Ref v = frame.popStack();
            UnaryOpRef res = UnaryOpRef.of(opcode, v);
            frame.pushStack(res);
        } else if (opcode >= 96 && opcode < 132) {
            Ref v2 = frame.popStack();
            Ref v1 = frame.popStack();
            BinOpRef res = BinOpRef.of(opcode, v1, v2);
            frame.pushStack(res);
        }


        /*
        switch (opcode) {
            case Opcodes.IADD:
                applyBinOp((a, b) -> cache.get((int) a + (int) b), RefType.INT);
                break;
            case Opcodes.LADD:
                applyBinOp((a, b) -> cache.get((long) a + (long) b), RefType.LONG);
                break;
            case Opcodes.FADD:
                applyBinOp((a, b) -> cache.get((float) a + (float) b), RefType.FLOAT);
                break;
            case Opcodes.DADD:
                applyBinOp((a, b) -> cache.get((double) a + (double) b), RefType.DOUBLE);
                break;
            case Opcodes.ISUB:
                applyBinOp((a, b) -> cache.get((int) a - (int) b), RefType.INT);
                break;
            case Opcodes.LSUB:
                applyBinOp((a, b) -> cache.get((long) a - (long) b), RefType.LONG);
                break;
            case Opcodes.FSUB:
                applyBinOp((a, b) -> cache.get((float) a - (float) b), RefType.FLOAT);
                break;
            case Opcodes.DSUB:
                applyBinOp((a, b) -> cache.get((double) a - (double) b), RefType.DOUBLE);
                break;
            case Opcodes.IMUL:
                applyBinOp((a, b) -> cache.get((int) a * (int) b), RefType.INT);
                break;
            case Opcodes.LMUL:
                applyBinOp((a, b) -> cache.get((long) a * (long) b), RefType.LONG);
                break;
            case Opcodes.FMUL:
                applyBinOp((a, b) -> cache.get((float) a * (float) b), RefType.FLOAT);
                break;
            case Opcodes.DMUL:
                applyBinOp((a, b) -> cache.get((double) a * (double) b), RefType.DOUBLE);
                break;
            case Opcodes.IDIV:
                applyBinOp((a, b) -> cache.get((int) a / (int) b), RefType.INT);
                break;
            case Opcodes.LDIV:
                applyBinOp((a, b) -> cache.get((long) a / (long) b), RefType.LONG);
                break;
            case Opcodes.FDIV:
                applyBinOp((a, b) -> cache.get((float) a / (float) b), RefType.FLOAT);
                break;
            case Opcodes.DDIV:
                applyBinOp((a, b) -> cache.get((double) a / (double) b), RefType.DOUBLE);
                break;
            case Opcodes.IREM:
                applyBinOp((a, b) -> cache.get((int) a % (int) b), RefType.INT);
                break;
            case Opcodes.LREM:
                applyBinOp((a, b) -> cache.get((long) a % (long) b), RefType.LONG);
                break;
            case Opcodes.FREM:
                applyBinOp((a, b) -> cache.get((float) a % (float) b), RefType.FLOAT);
                break;
            case Opcodes.DREM:
                applyBinOp((a, b) -> cache.get((double) a % (double) b), RefType.DOUBLE);
                break;

            case Opcodes.INEG:
                applyUnaryOp(a -> cache.get(-(int) a), RefType.INT);
                break;
            case Opcodes.LNEG:
                applyUnaryOp(a -> cache.get(-(long) a), RefType.LONG);
                break;
            case Opcodes.FNEG:
                applyUnaryOp(a -> cache.get(-(float) a), RefType.FLOAT);
                break;
            case Opcodes.DNEG:
                applyUnaryOp(a -> cache.get(-(double) a), RefType.DOUBLE);
                break;

            case Opcodes.ISHL:
                applyBinOp((a, b) -> cache.get((int) a << (int) b), RefType.INT);
                break;
            case Opcodes.LSHL:
                applyBinOp((a, b) -> cache.get((long) a << (int) b), RefType.LONG);
                break;
            case Opcodes.ISHR:
                applyBinOp((a, b) -> cache.get((int) a >> (int) b), RefType.INT);
                break;
            case Opcodes.LSHR:
                applyBinOp((a, b) -> cache.get((int) a >> (int) b), RefType.LONG);
                break;
            case Opcodes.IUSHR:
                applyBinOp((a, b) -> cache.get((int) a >>> (int) b), RefType.INT);
                break;
            case Opcodes.LUSHR:
                applyBinOp((a, b) -> cache.get((long) a >>> (int) b), RefType.LONG);
                break;

            case Opcodes.IAND:
                applyBinOp((a, b) -> cache.get((int) a & (int) b), RefType.INT);
                break;
            case Opcodes.LAND:
                applyBinOp((a, b) -> cache.get((long) a & (long) b), RefType.LONG);
                break;
            case Opcodes.IOR:
                applyBinOp((a, b) -> cache.get((int) a | (int) b), RefType.INT);
                break;
            case Opcodes.LOR:
                applyBinOp((a, b) -> cache.get((long) a | (long) b), RefType.LONG);
                break;
            case Opcodes.IXOR:
                applyBinOp((a, b) -> cache.get((int) a ^ (int) b), RefType.INT);
                break;
            case Opcodes.LXOR:
                applyBinOp((a, b) -> cache.get((long) a ^ (long) b), RefType.LONG);
                break;
        }
        */
    }

}

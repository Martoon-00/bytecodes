package tree;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;
import scan.MethodRef;
import tree.effect.EffectsView;
import tree.value.LinkValue;
import tree.value.MethodParamValue;

public class ControlFlow extends Analyzer<LinkValue> {
    private final LolInterpreter interpreter;

    public ControlFlow(LolInterpreter interpreter) {
        super(interpreter);
        this.interpreter = interpreter;
    }

    @Override
    protected Frame<LinkValue> newFrame(int nLocals, int nStack) {
        return new LolFrame(nLocals, nStack);
    }

    @Override
    protected Frame<LinkValue> newFrame(Frame<? extends LinkValue> src) {
        return new LolFrame(src);
    }

    @Override
    protected void newControlFlowEdge(int insn, int successor) {
        System.out.println(insn + " -> " + successor);
        super.newControlFlowEdge(insn, successor);
    }

    @Override
    protected boolean newControlFlowExceptionEdge(int insn, int successor) {
        System.out.println("E: " + insn + " -> " + successor);
        return super.newControlFlowExceptionEdge(insn, successor);
    }

    @Override
    protected boolean newControlFlowExceptionEdge(int insn, TryCatchBlockNode tcb) {
//        System.out.println("E: " + insn + " -> " + tcb.start + "-" + tcb.end);
        return super.newControlFlowExceptionEdge(insn, tcb);
    }

    @Override
    public Frame<LinkValue>[] analyze(String owner, MethodNode m) throws AnalyzerException {
        Frame<LinkValue>[] results = super.analyze(owner, m);
        Frame<LinkValue> initFrame = results[0];

        MethodRef method = interpreter.getMethod();
        Type[] argTypes = Type.getArgumentTypes(method.getDesc());
        int argNum = ((m.access & Opcodes.ACC_STATIC) == 0 ? 1 : 0) + argTypes.length;
        for (int i = 0; i < argNum; i++) {
            int finalI = i;
            initFrame.getLocal(i).replaceEntry(v -> new MethodParamValue(method, finalI, v.getType()));
        }
        return results;
    }

    public EffectsView getEffects() {
        return interpreter.getEffects().build();
    }
}

package tree;

import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.analysis.*;
import tree.value.ReplaceableValue;

public class ControlFlow extends Analyzer<ReplaceableValue> {
    int total = 0;

    public ControlFlow() {
        super(new LolInterpreter());
    }

    @Override
    protected Frame<ReplaceableValue> newFrame(int nLocals, int nStack) {
        return new LolFrame(nLocals, nStack);
    }

    @Override
    protected Frame<ReplaceableValue> newFrame(Frame<? extends ReplaceableValue> src) {
        return new LolFrame(src);
    }

    @Override
    protected void newControlFlowEdge(int insn, int successor) {
        System.out.println(insn + " -> " + successor);
        total++;
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

}

import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;

public class ControlFlow extends Analyzer<BasicValue> {
    int total = 0;

    public ControlFlow() {
        super(new LolInterpreter());
    }

    @Override
    protected Frame<BasicValue> newFrame(int nLocals, int nStack) {
        return super.newFrame(nLocals, nStack);
    }

    @Override
    protected Frame<BasicValue> newFrame(Frame<? extends BasicValue> src) {
        return super.newFrame(src);
    }

    @Override
    protected void newControlFlowEdge(int insn, int successor) {
        System.out.println(insn + " -> " + successor);
        total++;
        super.newControlFlowEdge(insn, successor);
    }

    @Override
    protected boolean newControlFlowExceptionEdge(int insn, int successor) {
        return super.newControlFlowExceptionEdge(insn, successor);
    }

    @Override
    protected boolean newControlFlowExceptionEdge(int insn, TryCatchBlockNode tcb) {
        return super.newControlFlowExceptionEdge(insn, tcb);
    }
}

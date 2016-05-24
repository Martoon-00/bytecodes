package tree;

import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Interpreter;
import tree.value.AltValue;
import tree.value.LinkValue;

public class LolFrame extends Frame<LinkValue> {

    public LolFrame(int nLocals, int nStack) {
        super(nLocals, nStack);
    }

    public LolFrame(Frame<? extends LinkValue> src) {
        super(src);
    }

    @Override
    public boolean merge(Frame<? extends LinkValue> frame, Interpreter<LinkValue> interpreter)
            throws AnalyzerException {
        if (getLocals() != frame.getLocals() || getStackSize() != frame.getStackSize())
            throw new AnalyzerException(null, "Merged frames have different stack size or locals number");

        // locals
        for (int i = 0; i < getLocals(); i++) {
            int finalI = i;
            getLocal(i).replaceEntry(v -> AltValue.of(v, frame.getLocal(finalI)));
        }
        // stack
        for (int i = 0; i < frame.getStackSize(); i++) {
            int finalI = i;
            getStack(i).replaceEntry(v -> AltValue.of(v, frame.getStack(finalI)));
        }
        return false;
    }

    @Override
    public boolean merge(Frame<? extends LinkValue> frame, boolean[] access) {
        // TODO:
        return super.merge(frame, access);
    }

}

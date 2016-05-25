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
        // wrap to links, needed on moving forward
        for (int i = 0; i < getLocals(); i++) {
            setLocal(i, LinkValue.of(getLocal(i)));
        }
        LinkValue[] stack = new LinkValue[getStackSize()];
        for (int i = 0; i < getStackSize(); i++) {
            stack[i] = LinkValue.of(getStack(i));
        }
        clearStack();
        for (LinkValue v : stack) {
            push(v);
        }
    }

    @Override
    public boolean merge(Frame<? extends LinkValue> frame, Interpreter<LinkValue> interpreter)
            throws AnalyzerException {
        if (getLocals() != frame.getLocals() || getStackSize() != frame.getStackSize())
            throw new AnalyzerException(null, "Merged frames have different stack size or locals number");

        // locals
        for (int i = 0; i < getLocals(); i++) {
            merge(getLocal(i), frame.getLocal(i));
        }
        // stack
        for (int i = 0; i < frame.getStackSize(); i++) {
            merge(getStack(i), frame.getStack(i));
        }
        return false;
    }

    private void merge(LinkValue v, LinkValue w) {
        // for case of same links
        if (v == w)
            return;
        v.replaceEntry(x -> LinkValue.of(AltValue.of(x, w)));
    }

    @Override
    public boolean merge(Frame<? extends LinkValue> frame, boolean[] access) {
        // TODO:
        return super.merge(frame, access);
    }

}

package tree;

import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Interpreter;
import scan.MethodRef;
import scan.except.InvalidBytecodeException;
import tree.value.AltValue;
import tree.value.LinkValue;

public class LolFrame extends Frame<LinkValue> {
    private final MethodRef method;

    public LolFrame(int nLocals, int nStack, MethodRef method) {
        super(nLocals, nStack);
        this.method = method;
    }

    public LolFrame(Frame<? extends LinkValue> src, MethodRef method) {
        super(src);
        this.method = method;
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
        return merge(frame);
    }

    private boolean merge(Frame<? extends LinkValue> frame) {
        if (getLocals() != frame.getLocals() || getStackSize() != frame.getStackSize())
            throw new InvalidBytecodeException(method, "Merged frames have different stack size or locals number");

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

    @Override
    public boolean merge(Frame<? extends LinkValue> frame, boolean[] access) {
        // TODO:
        return this.merge(frame);
//        return super.merge(frame, access);
    }

    private void merge(LinkValue v, LinkValue w) {
        // for case of same links
//        if (v == w)
//            return;
        // FIXME: this would break link system if some values would be same even when derive from different sources
        // FIXME: (like NoValue.val instead of new NoValue())
        if (LinkValue.sameValue(v, w))
            return;
        v.replaceEntry(x -> LinkValue.of(AltValue.of(x, w)));
    }

}

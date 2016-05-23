package tree;

import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Interpreter;
import tree.value.AltValue;
import tree.value.ReplaceableValue;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LolFrame extends Frame<ReplaceableValue> {

    public LolFrame(int nLocals, int nStack) {
        super(nLocals, nStack);
    }

    public LolFrame(Frame<? extends ReplaceableValue> src) {
        super(src);
    }

    @Override
    public boolean merge(Frame<? extends ReplaceableValue> frame, Interpreter<ReplaceableValue> interpreter)
            throws AnalyzerException {
        if (getLocals() != frame.getLocals() || getStackSize() != frame.getStackSize())
            throw new AnalyzerException(null, "Merged frames have different stack size or locals number");

        // locals
        for (int i = 0; i < getLocals(); i++) {
            int finalI = i;
            getLocal(i).replaceEntry(v -> AltValue.of(v.getType(), v, frame.getLocal(finalI)));
        }
        // stack
        List<BasicValue> stack = Stream.iterate(0, a -> a + 1).limit(getStackSize())
                .map(i -> pop())
                .collect(Collectors.toList());
        for (int i = 0; i < frame.getStackSize(); i++) {
            int finalI = i;
            getStack(i).replaceEntry(v -> AltValue.of(v.getType(), v, frame.getStack(finalI)));
        }
        return false;
    }

    @Override
    public boolean merge(Frame<? extends ReplaceableValue> frame, boolean[] access) {
        // TODO:
        return super.merge(frame, access);
    }
}

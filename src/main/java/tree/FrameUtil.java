package tree;

import org.objectweb.asm.tree.analysis.Frame;
import tree.value.MyValue;
import tree.value.Replaceable;

import java.util.function.Function;

public class FrameUtil {

    public static void map(Frame<? extends Replaceable> frame, Function<MyValue, MyValue> mapper) {
        for (int i = 0; i < frame.getLocals(); i++) {
            frame.getLocal(i).replaceEntry(mapper);
        }
        for (int i = 0; i < frame.getStackSize(); i++) {
            frame.getStack(i).replaceEntry(mapper);
        }
    }
}

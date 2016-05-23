package scan.noop;

import org.objectweb.asm.Opcodes;
import scan.frame.Frame;
import scan.except.IllegalStackManipulationException;
import scan.ref.Ref;

import java.util.function.Supplier;

class StackModInst {
    private final Supplier<Frame> frame;

    public StackModInst(Supplier<Frame> frame) {
        this.frame = frame;
    }

    public void apply(int opcode) {
        Frame frame = this.frame.get();
        switch (opcode) {
            case Opcodes.POP:
                pop(frame);
                break;
            case Opcodes.POP2:
                pop2(frame);
                break;
            case Opcodes.DUP:
                dup(frame);
                break;
            case Opcodes.DUP_X1:
                dup_x1(frame);
                break;
            case Opcodes.DUP_X2:
                dup_x2(frame);
                break;
            case Opcodes.DUP2:
                dup2(frame);
                break;
            case Opcodes.DUP2_X1:
                dup2_x1(frame);
                break;
            case Opcodes.DUP2_X2:
                dup2_x2(frame);
                break;
            case Opcodes.SWAP:
                swap(frame);
                break;
        }
    }

    private void dup(Frame frame) {
        Ref val = frame.popStack();
        ensureCategory1(val);
        frame.pushStack(val);
        frame.pushStack(val);
    }

    private void pop(Frame frame) {
        Ref v = frame.popStack();
        ensureCategory1(v);
    }

    private void pop2(Frame frame) {
        Ref v = frame.popStack();
        if (!v.getType().hasDoubleSize()) {
            pop(frame);
        }
    }

    private void dup_x1(Frame frame) {
        Ref v1 = frame.popStack();
        ensureCategory1(v1);
        Ref v2 = frame.popStack();
        ensureCategory1(v2);
        frame.pushStack(v1);
        frame.pushStack(v2);
        frame.pushStack(v1);
    }

    private void dup_x2(Frame frame) {
        Ref v1 = frame.popStack();
        Ref v2 = frame.popStack();
        ensureCategory1(v1);
        if (v2.getType().hasDoubleSize()) {
            frame.pushStack(v1);
            frame.pushStack(v2);
            frame.pushStack(v1);
        } else {
            Ref v3 = frame.popStack();
            frame.pushStack(v1);
            frame.pushStack(v3);
            frame.pushStack(v2);
            frame.pushStack(v1);
        }
    }

    private void dup2(Frame frame) {
        Ref v1 = frame.popStack();
        if (v1.getType().hasDoubleSize()) {
            frame.pushStack(v1);
            frame.pushStack(v1);
        } else {
            Ref v2 = frame.popStack();
            ensureCategory1(v2);
            frame.pushStack(v2);
            frame.pushStack(v1);
            frame.pushStack(v2);
            frame.pushStack(v1);
        }
    }

    private void dup2_x1(Frame frame) {
        Ref v1 = frame.popStack();
        Ref v2 = frame.popStack();
        ensureCategory1(v2);

        if (v1.getType().hasDoubleSize()) {
            frame.pushStack(v1);
            frame.pushStack(v2);
            frame.pushStack(v1);
        } else {
            Ref v3 = frame.popStack();
            ensureCategory1(v3);
            frame.pushStack(v2);
            frame.pushStack(v1);
            frame.pushStack(v3);
            frame.pushStack(v2);
            frame.pushStack(v1);
        }
    }

    private void dup2_x2(Frame frame) {
        Ref v1 = frame.popStack();
        Ref v2 = frame.popStack();
        if (v1.getType().hasDoubleSize()) {
            if (v2.getType().hasDoubleSize()) {
                frame.pushStack(v1);
                frame.pushStack(v2);
                frame.pushStack(v1);
            } else {
                Ref v3 = frame.popStack();
                ensureCategory1(v3);
                frame.pushStack(v1);
                frame.pushStack(v3);
                frame.pushStack(v2);
                frame.pushStack(v1);
            }
        } else {
            ensureCategory1(v2);
            Ref v3 = frame.popStack();
            if (v3.getType().hasDoubleSize()) {
                frame.pushStack(v2);
                frame.pushStack(v1);
                frame.pushStack(v3);
                frame.pushStack(v2);
                frame.pushStack(v1);
            } else {
                Ref v4 = frame.popStack();
                frame.pushStack(v2);
                frame.pushStack(v1);
                frame.pushStack(v4);
                frame.pushStack(v3);
                frame.pushStack(v2);
                frame.pushStack(v1);
            }
        }
    }

    private void swap(Frame frame) {
        Ref v1 = frame.popStack();
        ensureCategory1(v1);
        Ref v2 = frame.popStack();
        ensureCategory1(v2);
        frame.pushStack(v2);
        frame.pushStack(v1);
    }

    private void ensureCategory1(Ref v) {
        if (v.getType().hasDoubleSize()) {
            throw new IllegalStackManipulationException();
        }
    }
}

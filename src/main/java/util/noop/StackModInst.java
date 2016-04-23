package util.noop;

import org.objectweb.asm.Opcodes;
import util.Frame;
import util.except.IllegalStackManipulationException;
import util.ref.Ref;

public class StackModInst {
    private final Frame frame;

    public StackModInst(Frame frame) {
        this.frame = frame;
    }

    public void apply(int opcode) {
        switch (opcode) {
            case Opcodes.POP:
                pop();
                break;
            case Opcodes.POP2:
                pop2();
                break;
            case Opcodes.DUP:
                dup();
                break;
            case Opcodes.DUP_X1:
                dup_x1();
                break;
            case Opcodes.DUP_X2:
                dup_x2();
                break;
            case Opcodes.DUP2:
                dup2();
                break;
            case Opcodes.DUP2_X1:
                dup2_x1();
                break;
            case Opcodes.DUP2_X2:
                dup2_x2();
                break;
            case Opcodes.SWAP:
                swap();
                break;
        }
    }

    private void dup() {
        Ref val = frame.popStack();
        ensureCategory1(val);
        frame.pushStack(val);
        frame.pushStack(val);
    }

    private void pop() {
        Ref v = frame.popStack();
        ensureCategory1(v);
    }

    private void pop2() {
        Ref v = frame.popStack();
        if (!v.getType().hasDoubleSize()) {
            pop();
        }
    }

    private void dup_x1() {
        Ref v1 = frame.popStack();
        ensureCategory1(v1);
        Ref v2 = frame.popStack();
        ensureCategory1(v2);
        frame.pushStack(v1);
        frame.pushStack(v2);
        frame.pushStack(v1);
    }

    private void dup_x2() {
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

    private void dup2() {
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

    private void dup2_x1() {
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

    private void dup2_x2() {
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

    private void swap() {
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

package util.frame;

import util.MethodRef;
import util.except.InvalidBytecodeException;
import util.ref.PackRef;
import util.ref.Ref;
import util.ref.consts.NoRef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MutableFrame implements Frame {
    private final MethodRef method;

    private List<Ref> stack;
    private List<Ref> local;
    private boolean inVacuum = false;

    private MutableFrame(MethodRef method, List<Ref> stack, List<Ref> local) {
        this.method = method;
        this.stack = stack;
        this.local = local;
    }

    public MutableFrame(MethodRef method, List<Ref> local) {
        this.method = method;
        this.stack = new ArrayList<>();
        this.local = local;
    }

    public void pushStack(Ref ref) {
        stack.add(ref);
    }

    public Ref popStack() {
        if (inVacuum)
            return new NoRef("Acquired reference from dead code");

        int size = stack.size();
        List<Ref> last = stack.subList(size - 1, size);
        Ref res = last.get(0);
        last.clear();
        return res;
    }

    public void replaceStack(int n, Ref... refs) {
        if (inVacuum)
            return;

        int size = stack.size();
        List<Ref> last = stack.subList(size - n, size);
        last.clear();
        Collections.addAll(last, refs);
    }

    public Ref getLocal(int i) {
        if (inVacuum)
            return new NoRef("Acquired reference from dead code");

        return local.get(i);
    }

    public void setLocal(int i, Ref ref) {
        if (inVacuum)
            return;

        if (i < local.size()) {
            local.set(i, ref);
        } else {
            ensureLocalSize(i - 1);
            local.add(ref);
        }
    }

    @Override
    public boolean isInVacuum() {
        return inVacuum;
    }

    @Override
    public void setInVacuum() {
        inVacuum = true;
    }

    private void ensureLocalSize(int n) {
        while (local.size() <= n) {
            local.add(new NoRef(String.format("Undefined value as %d local value in %s method", local.size() - 1, method)));
        }
    }

    public MutableFrame copy() {
        return new MutableFrame(
                method,
                new ArrayList<>(stack),
                new ArrayList<>(local)
        );
    }

    @Override
    public void merge(Frame o) {
        if (!(o instanceof MutableFrame))
            throw new IllegalArgumentException("Merge of frames with different types");
        MutableFrame other = (MutableFrame) o;

        if (o.isInVacuum())
            return;
        if (isInVacuum()) {
            stack = other.stack;
            local = other.local;
            inVacuum = false;
            return;
        }

        // stack
        if (stack.size() != other.stack.size())
            throw new InvalidBytecodeException(method, "Stacks from different branches has different sized");
        for (int i = 0; i < stack.size(); i++) {
            stack.set(i, PackRef.ofTwo(stack.get(i), other.stack.get(i)));
        }

        // locals
        int n = Math.min(local.size(), other.local.size());
        for (int i = 0; i < n; i++) {
            local.set(i, PackRef.ofTwo(local.get(i), other.local.get(i)));
        }
        for (int i = n; i < other.stack.size(); i++) {
            stack.add(other.stack.get(i));
        }
    }
}

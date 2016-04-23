package util;

import util.ref.Ref;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Frame {
    private final String funcName;

    private final List<Ref> stack = new ArrayList<>();
    private final List<Ref> local;

    public Frame(String funcName, List<Ref> local) {
        this.funcName = funcName;
        this.local = local;
    }

    public void pushStack(Ref ref) {
        stack.add(ref);
    }

    public Ref popStack(){
        int size = stack.size();
        List<Ref> last = stack.subList(size - 1, size);
        Ref res = last.get(0);
        last.clear();
        return res;
    }

    public void replaceStack(int n, Ref... refs) {
        int size = stack.size();
        List<Ref> last = stack.subList(size - n, size);
        last.clear();
        Collections.addAll(last, refs);
    }

    public Ref getLocal(int i) {
        return local.get(i);
    }

    public void setLocal(int i, Ref ref) {
        local.set(i, ref);
    }

    public Frame pass(String funcName, int args) {
        return new Frame(funcName, local.subList(local.size() - args, local.size()));
    }
}

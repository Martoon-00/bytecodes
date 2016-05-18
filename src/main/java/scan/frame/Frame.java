package scan.frame;

import scan.ref.Ref;

public interface Frame {
    void pushStack(Ref ref);

    Ref popStack();

    void replaceStack(int n, Ref... refs);

    Ref getLocal(int i);

    void setLocal(int i, Ref ref);

    Frame copy();

    void merge(Frame other);

    void invalidatingMerge(Frame other);

    boolean isInVacuum();

    void setInVacuum();
}

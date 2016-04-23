package util.ref;

import java.util.List;

public class Either implements Ref {
    private final List<Ref> refs;

    public Either(List<Ref> refs) {
        this.refs = refs;
    }
}

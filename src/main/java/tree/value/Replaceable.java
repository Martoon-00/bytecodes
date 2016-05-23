package tree.value;

import java.util.function.Function;

public interface Replaceable {
    void replaceEntry(Function<MyValue, MyValue> map);
}

package tree.value;

import org.objectweb.asm.Type;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AltValue extends MyValue {
    private final Set<MyValue> alternatives;

    public AltValue(Type type, Set<MyValue> alternatives) {
        super(type);
        this.alternatives = alternatives;
    }

    public static MyValue of(MyValue... values) {
        HashSet<MyValue> alternatives = new HashSet<>();
        for (MyValue value : values) {
            if (value instanceof AltValue)
                alternatives.addAll(((AltValue) value).alternatives);
            else
                alternatives.add(value);
        }
        if (alternatives.isEmpty())
            return new NoValue();

        MyValue single = alternatives.iterator().next();
        if (alternatives.size() == 1)
            return single;

        for (MyValue alternative : alternatives) {
            MyValue.assertSameType(single, alternative);
        }
        return new AltValue(single.getType(), alternatives);
    }

    @Override
    protected MyValue proceedElimRec(Set<MyValue> visited, boolean complicated) {
        Set<MyValue> res = alternatives.stream()
                .map(alt -> alt.eliminateRecursion(visited, complicated))
                .collect(Collectors.toSet());
        return new AltValue(getType(), res);
    }

    @Override
    public MyValue simplify() {
        Set<MyValue> alts = alternatives.stream()
                .map(MyValue::simplify)
                .filter(v -> !(v instanceof NoValue))
                .flatMap(v -> v instanceof AltValue ? ((AltValue) v).alternatives.stream() : Stream.of(v))
                .collect(Collectors.toSet());
        for (MyValue alt : alts) {
            if (alt instanceof AnyValue) {
                return new AnyValue(getType());
            }
        }
        return new AltValue(getType(), alts);
    }

    @Override
    public String toString() {
        if (alternatives.isEmpty())
            return "<no alt>";
        return alternatives.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" | "));
    }
}

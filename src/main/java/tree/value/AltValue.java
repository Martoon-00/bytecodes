package tree.value;

import intra.IntraContext;
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

    public Stream<MyValue> getAlternatives() {
        return alternatives.stream();
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
        if (alts.stream().anyMatch(v -> v instanceof AnyValue))
            return new AnyValue(getType());

        for (MyValue alt : alts) {
            if (alt instanceof AnyValue) {
                return new AnyValue(getType());
            }
        }
        return new AltValue(getType(), alts);
    }

    @Override
    public MyValue resolveReferences(IntraContext context, int depth) {
        return AltValue.of(alternatives.stream()
                .map(v -> v.resolveReferences(context, depth))
                .toArray(MyValue[]::new));
    }

    @Override
    public MyValue eliminateReferences() {
        return AltValue.of(alternatives.stream()
                .map(MyValue::eliminateReferences)
                .toArray(MyValue[]::new));
    }

    @Override
    public MyValue copy() {
        return new AltValue(getType(), alternatives);
    }

    public static boolean isConstSet(MyValue value) {
        return value instanceof AltValue
                && ((AltValue) value).getAlternatives().allMatch(v -> v instanceof ConstValue);
    }

    @Override
    public String toString() {
        if (alternatives.isEmpty())
            return "<no alt>";
        String res = alternatives.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" | "));
        return "(" + res + ")";
    }
}

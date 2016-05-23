package tree.value;

import org.objectweb.asm.Type;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AltValue extends MyValue {
    private final Set<MyValue> alternatives;

    public AltValue(Type type, Set<MyValue> alternatives) {
        super(type);
        this.alternatives = alternatives;
    }

    public static MyValue of(Type type, MyValue... values) {
//        AltValue res = new AltValue(type);
        HashSet<MyValue> alternatives = new HashSet<>();
        for (MyValue value : values) {
            if (value instanceof AltValue)
                alternatives.addAll(((AltValue) value).alternatives);
            else
                alternatives.add(value);
        }
        if (alternatives.size() == 1)
            return alternatives.iterator().next();
        return new AltValue(type, alternatives);
    }

    @Override
    protected MyValue proceedElimRec(Set<MyValue> visited, boolean complicated) {
        Set<MyValue> res = alternatives.stream()
                .map(alt -> alt.eliminateRecursion(visited, complicated))
                .collect(Collectors.toSet());
        return new AltValue(getType(), res);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AltValue altValue = (AltValue) o;

        return alternatives.equals(altValue.alternatives);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + alternatives.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return alternatives.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" | "));
    }
}

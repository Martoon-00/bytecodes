package scan.ref;

import scan.RefType;
import scan.except.InvalidBytecodeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class PackRef extends Ref {
    private final Ref[] values;

    private PackRef(RefType type, Ref[] values) {
        super(type);
        this.values = values;
    }

    public static Ref of(RefType type, Ref... values) {
        if (values.length == 0)
            return new EmptyRef();

        ArrayList<Ref> res = new ArrayList<>();
        for (Ref value : values) {
            if (value instanceof PackRef) {
                Collections.addAll(res, (((PackRef) value).getValues()));
            } else {
                res.add(value);
            }
        }

        return new PackRef(type, res.toArray(new Ref[res.size()]));
    }

    public static Ref of(Ref... values) {
        if (values.length == 0)
            return new EmptyRef();
        if (values.length == 1) {
            return values[0];
        }

        for (int i = 1; i < values.length; i++) {
            ensureCommonTypes(values[i], values[0]);
        }
        return PackRef.of(values[0].getType(), values);
    }

    public static Ref ofTwo(Ref a, Ref b) {
        if (a == b) {
            return a;
        }
        ensureCommonTypes(a, b);

        return PackRef.of(a.getType(), a, b);
    }

    private static void ensureCommonTypes(Ref a, Ref b) {
        if (a.getType() != b.getType())
            throw new InvalidBytecodeException("Values have different types: " + a.getType() + " vs " + b.getType());
    }

    public Ref[] getValues() {
        return values;
    }

    @Override
    public String show() {
        String entry = Arrays.stream(values)
                .map(Object::toString)
                .collect(Collectors.joining(" | "));
        return "(" + entry + ")";
    }
}

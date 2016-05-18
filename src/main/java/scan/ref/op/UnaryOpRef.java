package scan.ref.op;

import scan.RefType;
import scan.except.UnsupportedOpcodeException;
import scan.ref.Ref;

public abstract class UnaryOpRef extends Ref {
    protected final int opcode;
    protected final Ref a;

    public UnaryOpRef(int opcode, Ref a) {
        super(null);
        this.opcode = opcode;
        this.a = a;
    }

    public static UnaryOpRef of(int opcode, Ref a) {
        // 116 - 119:  unary -
        // 136 - 147:  casts
        if (opcode >= 116 && opcode < 120) {
            return new NegUnaryOpRef(opcode, a);
        } else if (opcode >= 133 && opcode < 148) {
            return new CastUnaryOpRef(opcode, a);
        } else throw new UnsupportedOpcodeException(opcode);
    }

    @Override
    public RefType getType() {
        return evalType();
    }

    protected abstract RefType evalType();

}

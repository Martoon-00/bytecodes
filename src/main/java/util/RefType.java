package util;


import org.objectweb.asm.Type;

public enum RefType {
    BOOLEAN(false),
    BYTE(false),
    SHORT(false),
    CHAR(false),
    INT(false),
    LONG(true),
    FLOAT(false),
    DOUBLE(true),
    OBJECTREF(false);

    /**
     * Whether type is of double size comparing to object reference size (1 or 2 category)
     * <link>https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-2.html#jvms-2.11.1</link>
     */
    private final boolean dsize;

    RefType(boolean dsize) {
        this.dsize = dsize;
    }

    public boolean hasDoubleSize() {
        return dsize;
    }

    public static RefType fromAsmType(Type type) {
        switch (type.toString()) {
            case "Z":
                return BOOLEAN;
            case "B":
                return BYTE;
            case "S":
                return SHORT;
            case "C":
                return CHAR;
            case "I":
                return INT;
            case "L":
                return LONG;
            case "F":
                return FLOAT;
            case "D":
                return DOUBLE;
            default:
                return OBJECTREF;
        }
    }
}

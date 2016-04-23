package util.except;

public class OpcodeNotSupportedException extends RuntimeException {
    private final int opcode;

    public OpcodeNotSupportedException(int opcode) {
        super(String.format("Opcode %d is not supported", opcode));
        this.opcode = opcode;
    }

    public int getOpcode() {
        return opcode;
    }
}

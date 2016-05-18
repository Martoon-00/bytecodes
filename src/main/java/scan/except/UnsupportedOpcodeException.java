package scan.except;

public class UnsupportedOpcodeException extends RuntimeException {
    private final int opcode;

    public UnsupportedOpcodeException(int opcode) {
        super(String.format("Opcode %d is not supported", opcode));
        this.opcode = opcode;
    }

    public int getOpcode() {
        return opcode;
    }
}

package util.except;

import util.MethodRef;

public class InvalidBytecodeException extends RuntimeException {
    private final MethodRef method;

    public InvalidBytecodeException(MethodRef method) {
        super("Invalid bytecode found at method " + method);
        this.method = method;
    }

    public InvalidBytecodeException(MethodRef method, String message) {
        super(message);
        this.method = method;
    }

    public InvalidBytecodeException() {
        method = null;
    }

    public InvalidBytecodeException(String message) {
        super(message);
        method = null;
    }

    public MethodRef getMethod() {
        return method;
    }
}

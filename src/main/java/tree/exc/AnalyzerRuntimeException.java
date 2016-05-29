package tree.exc;

public class AnalyzerRuntimeException extends RuntimeException{
    public AnalyzerRuntimeException() {
    }

    public AnalyzerRuntimeException(String message) {
        super(message);
    }

    public AnalyzerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnalyzerRuntimeException(Throwable cause) {
        super(cause);
    }

    public AnalyzerRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

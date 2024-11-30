package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidMapException extends RuntimeException {
    public InvalidMapException(String message) {
        super(message);
    }

    public InvalidMapException(String message, Throwable cause) {
        super(message, cause);
    }
}

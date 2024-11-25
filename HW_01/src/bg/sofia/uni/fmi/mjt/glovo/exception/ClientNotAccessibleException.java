package bg.sofia.uni.fmi.mjt.glovo.exception;

public class ClientNotAccessibleException extends RuntimeException {
    public ClientNotAccessibleException(String message) {
        super(message);
    }

    public ClientNotAccessibleException(String message, Throwable cause) {
        super(message, cause);
    }
}

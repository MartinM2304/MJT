package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class ResponseIncorrectException extends RuntimeException {
    public ResponseIncorrectException(String message) {
        super(message);
    }

    public ResponseIncorrectException(String message, Throwable cause) {
        super(message, cause);
    }
}

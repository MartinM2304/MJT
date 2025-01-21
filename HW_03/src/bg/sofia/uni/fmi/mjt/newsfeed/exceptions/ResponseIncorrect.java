package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class ResponseIncorrect extends RuntimeException{
    public ResponseIncorrect(String message) {
        super(message);
    }

    public ResponseIncorrect(String message, Throwable cause) {
        super(message, cause);
    }
}

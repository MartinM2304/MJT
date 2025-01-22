package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class NewsApiErrorException extends RuntimeException {
    public NewsApiErrorException(String message) {
        super(message);
    }

    public NewsApiErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}

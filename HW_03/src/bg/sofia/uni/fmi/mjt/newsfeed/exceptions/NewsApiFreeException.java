package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class NewsApiFreeException extends RuntimeException{
    public NewsApiFreeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NewsApiFreeException(String message) {
        super(message);
    }
}

package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class IllegalRequestParameter extends RuntimeException{

    public IllegalRequestParameter(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalRequestParameter(String message) {
        super(message);
    }
}

package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class JSONParsingException extends Exception{
    public JSONParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSONParsingException(String message) {
        super(message);
    }
}

package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

public class ClientSendException extends Exception{

    public ClientSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientSendException(String message) {
        super(message);
    }
}

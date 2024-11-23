package bg.sofia.uni.fmi.mjt.socialnetwork.exception;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

public class UserRegistrationException extends  RuntimeException{

    public UserRegistrationException(String message){
        super((message));
    }

    public UserRegistrationException(String message,Throwable cause){
        super(message,cause);
    }
}

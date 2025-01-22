package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IllegalRequestParameterTest {

    @Test
    public void testWithThrowable(){
        String message = "Test exception with cause";
        Throwable cause = new RuntimeException("Underlying cause");
        IllegalRequestParameter exception=new IllegalRequestParameter("message",cause);

        assertNotNull(exception,"Exception cant be null");
    }
}
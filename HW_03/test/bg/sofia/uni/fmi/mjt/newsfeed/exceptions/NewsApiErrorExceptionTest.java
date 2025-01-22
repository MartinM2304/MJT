package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NewsApiErrorExceptionTest {

    @Test
    public void testWithThrowable(){
        String message = "Test exception with cause";
        Throwable cause = new RuntimeException("Underlying cause");
        NewsApiErrorException exception=new NewsApiErrorException("message",cause);

        assertNotNull(exception,"Exception cant be null");
    }
}
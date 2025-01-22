package bg.sofia.uni.fmi.mjt.newsfeed.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientSendExceptionTest {
    @Test
    public void testMessageConstructor() throws ClientSendException {
        ClientSendException exception= new ClientSendException("message");
        assertNotNull(exception,"Exception cant be null");
    }
}
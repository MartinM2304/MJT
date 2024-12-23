package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testOfValidTransaction() {
        String validTransaction = "1,123,200.0,2024-01-01 12:00:00,NYC,ONLINE";

        Transaction transaction = Transaction.of(validTransaction);
        assertEquals("1", transaction.transactionID(), "Incorrect transaction ID");
        assertEquals("123", transaction.accountID(), "Incorrect account ID");
        assertEquals(200.0, transaction.transactionAmount(), 0.01, "Incorrect transaction amount");
        assertEquals(LocalDateTime.of(2024, 1, 1, 12, 0), transaction.transactionDate(),
                "Incorrect transaction date");
        assertEquals("NYC", transaction.location(), "Incorrect location");
        assertEquals(Channel.ONLINE, transaction.channel(), "Incorrect channel");
    }

    @Test
    void testOfInvalidTransactionWithNullString() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.of(null),
                "Expected exception for null transaction");
    }

    @Test
    void testOfInvalidTransactionWithWrongFields() {
        String invalidTransaction = "1,123,200.0,2024-01-01 12:00:00,NYC";

        assertThrows(IllegalArgumentException.class, () -> Transaction.of(invalidTransaction),
                "Expected exception for transaction with wrong fields count");
    }
}
package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SmallTransactionsRuleTest {

    @Test
    void testApplicable() {
        Transaction t1 = mock(Transaction.class);
        when(t1.transactionAmount()).thenReturn(5.0);

        Transaction t2 = mock(Transaction.class);
        when(t2.transactionAmount()).thenReturn(10.0);

        SmallTransactionsRule rule = new SmallTransactionsRule(2, 20.0, 0.5);
        assertTrue(rule.applicable(List.of(t1, t2)), "Rule should be applicable");
    }

    @Test
    void testWeight(){
        Transaction t1 = mock(Transaction.class);
        when(t1.transactionAmount()).thenReturn(5.0);

        Transaction t2 = mock(Transaction.class);
        when(t2.transactionAmount()).thenReturn(10.0);

        SmallTransactionsRule rule = new SmallTransactionsRule(2, 20.0, 0.5);
        assertEquals(0.5,rule.weight());
    }
}
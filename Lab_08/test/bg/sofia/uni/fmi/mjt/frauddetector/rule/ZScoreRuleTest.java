package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ZScoreRuleTest {

    @Test
    void testApplicable() {
        Transaction t1 = mock(Transaction.class);
        when(t1.transactionAmount()).thenReturn(200.0);

        Transaction t2 = mock(Transaction.class);
        when(t2.transactionAmount()).thenReturn(50.0);

        ZScoreRule rule = new ZScoreRule(1.5, 0.5);
        assertTrue(rule.applicable(List.of(t1, t2)));
    }

    @Test
    void tstWeight(){
        Transaction t1 = mock(Transaction.class);
        when(t1.transactionAmount()).thenReturn(200.0);

        Transaction t2 = mock(Transaction.class);
        when(t2.transactionAmount()).thenReturn(50.0);

        double weight=0.5;
        ZScoreRule rule = new ZScoreRule(1.5, weight);
        assertEquals(weight,rule.weight());
    }
}
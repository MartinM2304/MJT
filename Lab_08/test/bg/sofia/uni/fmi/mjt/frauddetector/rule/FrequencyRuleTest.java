package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import org.junit.jupiter.api.Test;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FrequencyRuleTest {

    @Test
    void testApplicableWithThresholdMet() {
        Transaction t1 = mock(Transaction.class);
        when(t1.transactionDate()).thenReturn(LocalDateTime.of(2024, 12, 1, 12, 0));

        Transaction t2 = mock(Transaction.class);
        when(t2.transactionDate()).thenReturn(LocalDateTime.of(2024, 12, 1, 12, 1));

        Transaction t3 = mock(Transaction.class);
        when(t3.transactionDate()).thenReturn(LocalDateTime.of(2024, 12, 1, 12, 2));

        FrequencyRule rule = new FrequencyRule(2, ChronoUnit.MINUTES.getDuration(), 0.7);
        assertTrue(rule.applicable(List.of(t1, t2, t3)), "Rule should be applicable when transaction count in the time window meets the threshold");
    }

    @Test
    void testApplicableWithThresholdNotMet() {
        Transaction t1 = mock(Transaction.class);
        when(t1.transactionDate()).thenReturn(LocalDateTime.of(2024, 12, 1, 12, 0));

        Transaction t2 = mock(Transaction.class);
        when(t2.transactionDate()).thenReturn(LocalDateTime.of(2024, 12, 1, 13, 0));

        FrequencyRule rule = new FrequencyRule(2, ChronoUnit.MINUTES.getDuration(), 0.7);
        assertFalse(rule.applicable(List.of(t1, t2)), "Rule should not be applicable when transaction count in the time window does not meet the threshold");
    }

    @Test
    void testApplicableWithEmptyTransactions() {
        FrequencyRule rule = new FrequencyRule(1, ChronoUnit.MINUTES.getDuration(), 0.7);
        assertFalse(rule.applicable(List.of()), "Rule should not be applicable with empty transaction list");
    }

    @Test
    void testWeight() {
        FrequencyRule rule = new FrequencyRule(3, ChronoUnit.MINUTES.getDuration(), 0.8);
        assertTrue(rule.weight() == 0.8, "Weight should match the value set in the constructor");
    }
}
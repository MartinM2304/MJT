package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocationsRuleTest {
    @Test
    void testApplicableWithThresholdMet() {
        Transaction t1 = mock(Transaction.class);
        when(t1.location()).thenReturn("New York");

        Transaction t2 = mock(Transaction.class);
        when(t2.location()).thenReturn("Boston");

        Transaction t3 = mock(Transaction.class);
        when(t3.location()).thenReturn("San Francisco");

        LocationsRule rule = new LocationsRule(2, 0.5);
        assertTrue(rule.applicable(List.of(t1, t2, t3)), "Rule should be applicable when distinct locations meet the threshold");
    }

    @Test
    void testApplicableWithThresholdNotMet() {
        Transaction t1 = mock(Transaction.class);
        when(t1.location()).thenReturn("New York");

        Transaction t2 = mock(Transaction.class);
        when(t2.location()).thenReturn("New York");

        LocationsRule rule = new LocationsRule(2, 0.5);
        assertFalse(rule.applicable(List.of(t1, t2)), "Rule should not be applicable when distinct locations do not meet the threshold");
    }

    @Test
    void testApplicableWithEmptyTransactions() {
        LocationsRule rule = new LocationsRule(1, 0.5);
        assertFalse(rule.applicable(List.of()), "Rule should not be applicable with empty transaction list");
    }

    @Test
    void testWeight() {
        LocationsRule rule = new LocationsRule(2, 0.8);
        assertTrue(rule.weight() == 0.8, "Weight should match the value set in the constructor");
    }
}
package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionAnalyzerImplTest {

    private static final String TRANSACTIONS_DATA = """
            transactionID,accountID,transactionAmount,transactionDate,location,channel
            1,AC00128,200.0,2024-04-02 12:00:00,San Diego,ATM
            2,AC00455,50.0,2024-01-01 13:00:00,NYC,ATM
            3,AC00019,30.0,2024-01-10 12:30:00,BOS,BRANCH
            4,AC00070,10.0,2024-09-03 12:00:00,SFO,ONLINE
            """;
    private List<Rule> rules;
    private TransactionAnalyzerImpl analyzer;

    @BeforeEach
    void setUp() {
        Rule mockRule = mock(Rule.class);
        when(mockRule.weight()).thenReturn(1.0);
        when(mockRule.applicable(Mockito.anyList())).thenReturn(true);

        rules = List.of(mockRule);
        analyzer = new TransactionAnalyzerImpl(new StringReader(TRANSACTIONS_DATA), rules);
    }

    @Test
    void testAllTransactions() {
        List<Transaction> transactions = analyzer.allTransactions();
        assertEquals(4, transactions.size(), "Incorrect number of transactions");
    }

    @Test
    void testAllAccountIDs() {
        List<String> accountIDs = analyzer.allAccountIDs();
        assertEquals(3, accountIDs.size(), "Incorrect number of unique account IDs");
    }

    @Test
    void testTransactionCountByChannel() {
        Map<Channel, Integer> counts = analyzer.transactionCountByChannel();
        assertEquals(2, counts.get(Channel.ONLINE));
        assertEquals(1, counts.get(Channel.ATM));
    }

    @Test
    void testAmountSpentByUser() {
        double amount = analyzer.amountSpentByUser("AC00128");
        assertEquals(200.0, amount, 0.01);
    }

    @Test
    void testAmountSpentByUserWithInvalidAccountId() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.amountSpentByUser(""));
    }

    @Test
    void testAllTransactionsByUser() {
        List<Transaction> transactions = analyzer.allTransactionsByUser("AC00455");
        assertEquals(1, transactions.size());
    }

    @Test
    void testAllTransactionsByUserWithInvalidAccountId() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.allTransactionsByUser(""),
                "Expected exception for invalid account ID");
    }

    @Test
    void testAccountRating() {
        double rating = analyzer.accountRating("123");
        assertEquals(1.0, rating, 0.01, "Incorrect account rating");
    }

    @Test
    void testAccountsRisk() {
        SortedMap<String, Double> accountsRisk = analyzer.accountsRisk();
        assertEquals(4, accountsRisk.size(), "Incorrect number of accounts in risk map");
        assertEquals(1.0, accountsRisk.get("AC00128"), 0.01, "Incorrect risk score for account");
        assertEquals(1.0, accountsRisk.get("AC00455"), 0.01, "Incorrect risk score for account");
    }

    @Test
    void testConstructorWithInvalidRules() {
        Rule mockRule = mock(Rule.class);
        when(mockRule.weight()).thenReturn(0.5);
        List<Rule> invalidRules = List.of(mockRule, mockRule);

        assertThrows(IllegalArgumentException.class,
                () -> new TransactionAnalyzerImpl(new StringReader(TRANSACTIONS_DATA), invalidRules),
                "Expected exception for invalid rule weights");
    }

    @Test
    void testaccountRatingNull(){
        assertThrows(IllegalArgumentException.class,()->analyzer.accountRating(null));
    }
}
package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;
import java.util.TreeMap;

public class TransactionAnalyzerImpl implements TransactionAnalyzer {

    private List<Transaction> transactions;
    private List<Rule> rules;

    public TransactionAnalyzerImpl(Reader reader, List<Rule> rules) {
        if (rules.stream().mapToDouble(Rule::weight).sum() != 1.0) {
            throw new IllegalArgumentException("Sum of rule is not 1");
        }

        this.rules = rules;
        this.transactions = new BufferedReader(reader).lines()
                .skip(1)
                .map(Transaction::of)
                .toList();
    }

    @Override
    public List<Transaction> allTransactions() {
        return transactions;
    }

    @Override
    public List<String> allAccountIDs() {
        return transactions.stream()
                .map(Transaction::accountID)
                .distinct()
                .toList();
    }

    @Override
    public Map<Channel, Integer> transactionCountByChannel() {
        return transactions.stream()
                .collect(
                        Collectors.groupingBy(Transaction::channel, Collectors.summingInt(t -> 1)));
    }

    @Override
    public double amountSpentByUser(String accountID) {
        if (accountID == null || accountID.isEmpty() || accountID.isBlank()) {
            throw new IllegalArgumentException("Account ID is null/empy/blank");
        }
        return transactions.stream()
                .filter(t -> t.accountID().equals(accountID))
                .mapToDouble(Transaction::transactionAmount)
                .sum();
    }

    @Override
    public List<Transaction> allTransactionsByUser(String accountId) {
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("Account ID is null or empty");
        }
        return transactions.stream()
                .filter(t -> t.accountID().equals(accountId))
                .toList();
    }

    @Override
    public double accountRating(String accountId) {
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("Account ID is null or empty");
        }
        List<Transaction> userTransactions = allTransactionsByUser(accountId);
        return rules.stream()
                .filter(rule -> rule.applicable(userTransactions))
                .mapToDouble(Rule::weight)
                .sum();
    }

    @Override
    public SortedMap<String, Double> accountsRisk() {
        return transactions.stream()
                .map(Transaction::accountID)
                .distinct()
                .collect(Collectors.toMap(
                        accountId -> accountId,
                        this::accountRating,
                        (a, b) -> b,
                        TreeMap::new
                ));
    }
}

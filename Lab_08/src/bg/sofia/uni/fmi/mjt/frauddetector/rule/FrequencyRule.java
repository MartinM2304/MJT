package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

public class FrequencyRule implements Rule {

    private int transactionCountThreshold;
    private TemporalAmount timeWindow;
    private double weight;

    public FrequencyRule(int transactionCountThreshold, TemporalAmount timeWindow, double weight) {
        this.transactionCountThreshold = transactionCountThreshold;
        this.timeWindow = timeWindow;
        this.weight = weight;
    }

    /**
     * Determines whether the rule is applicable based on the given list of transactions.
     *
     * @param transactions the list of objects to evaluate.
     *                     These transactions are used to determine if the rule
     *                     conditions are satisfied.
     * @return true, if the rule is applicable based on the transactions.
     */
    public boolean applicable(List<Transaction> transactions) {
        return transactions.stream()
                .anyMatch(transaction -> {
                    LocalDateTime begin = transaction.transactionDate();
                    LocalDateTime end = transaction.transactionDate().plus(timeWindow);
                    long count = transactions.stream()
                            .filter(transaction1 -> !transaction1.transactionDate().isAfter(end)
                                    && !transaction1.transactionDate().isBefore(begin))
                            .count();
                    return count >= transactionCountThreshold;
                });
    }

    /**
     * Retrieves the weight of the rule.
     * The weight represents the importance or priority of the rule
     * and is a double-precision floating-point number in the interval [0, 1].
     *
     * @return the weight of the rule.
     */
    public double weight() {
        return weight;
    }
}

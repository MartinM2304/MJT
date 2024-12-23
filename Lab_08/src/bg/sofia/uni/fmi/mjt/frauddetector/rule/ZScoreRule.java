package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class ZScoreRule implements Rule {
    private final double zScoreThreshold;
    private final double weight;

    public ZScoreRule(double zScoreThreshold, double weight) {
        this.zScoreThreshold = zScoreThreshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        double mean = transactions.stream()
                .mapToDouble(Transaction::transactionAmount)
                .average().orElse(0.0);
        double variance = transactions.stream()
                .mapToDouble(t -> Math.pow(t.transactionAmount() - mean, 2))
                .average().orElse(0.0);
        double stdDeviation = Math.sqrt(variance);

        return transactions.stream()
                .mapToDouble(t -> Math.abs((t.transactionAmount() - mean) / stdDeviation))
                .anyMatch(z -> z > zScoreThreshold);
    }

    @Override
    public double weight() {
        return weight;
    }
}

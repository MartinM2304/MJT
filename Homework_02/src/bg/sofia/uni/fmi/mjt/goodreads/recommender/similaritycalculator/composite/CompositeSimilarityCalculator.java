package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Map;
import java.util.Set;

public class CompositeSimilarityCalculator implements SimilarityCalculator {

    private Map<SimilarityCalculator, Double> similarityCalculatorMap;

    public CompositeSimilarityCalculator(Map<SimilarityCalculator, Double> similarityCalculatorMap) {
        if (similarityCalculatorMap == null) {
            throw new IllegalArgumentException("similarityCalcMap is null");
        }
        this.similarityCalculatorMap = similarityCalculatorMap;
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("first or second is null");
        }
        double result = 0;
        Set<SimilarityCalculator> calculators = similarityCalculatorMap.keySet();
        for (SimilarityCalculator calculator : calculators) {
            result += (calculator.calculateSimilarity(first, second) * similarityCalculatorMap.get(calculator));
        }
        return result;
    }

}

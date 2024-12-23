package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TFIDFSimilarityCalculator implements SimilarityCalculator {

    Set<Book> books;
    TextTokenizer tokenizer;

    public TFIDFSimilarityCalculator(Set<Book> books, TextTokenizer tokenizer) {
        if (books == null || tokenizer == null) {
            throw new IllegalArgumentException("book or tokenizer is null");
        }
        this.books = books;
        this.tokenizer = tokenizer;
    }

    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("book is null");
        }
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
        validateBook(first);
        validateBook(second);
        Map<String, Double> tfIdfScoresFirst = computeTFIDF(first);
        Map<String, Double> tfIdfScoresSecond = computeTFIDF(second);

        return cosineSimilarity(tfIdfScoresFirst, tfIdfScoresSecond);
    }

    public Map<String, Double> computeTFIDF(Book book) {
        validateBook(book);
        Map<String, Double> tfScores = computeTF(book);
        Map<String, Double> idfScores = computeIDF(book);
        Map<String, Double> tfIdfScores = new HashMap<>();

        for (String word : tfScores.keySet()) {
            double tf = tfScores.get(word);
            double idf = idfScores.getOrDefault(word, 0.0);
            tfIdfScores.put(word, tf * idf);
        }

        return tfIdfScores;
    }

    public Map<String, Double> computeTF(Book book) {
        validateBook(book);
        List<String> description = tokenizer.tokenize(book.description());
        Map<String, Integer> wordFrequency = new HashMap<>();
        Map<String, Double> result = new HashMap<>();

        for (String token : description) {
            wordFrequency.put(token, wordFrequency.getOrDefault(token, 0) + 1);
        }
        int totalWords = description.size();
        Set<String> words = wordFrequency.keySet();
        for (String word : words) {
            int frequency = wordFrequency.get(word);
            result.put(word, (double) frequency / totalWords);
        }
        return result;
    }

    public Map<String, Double> computeIDF(Book book) {
        validateBook(book);
        Map<String, Double> idfScores = new HashMap<>();
        List<String> wordsInBook = tokenizer.tokenize(book.description());
        Set<String> uniqueWords = new HashSet<>(wordsInBook);
        int totalBooks = books.size();

        for (String word : uniqueWords) {
            int booksContainingWord = 0;

            for (Book b : books) {
                List<String> wordsInOtherBook = tokenizer.tokenize(b.description());
                if (wordsInOtherBook.contains(word)) {
                    booksContainingWord++;
                }
            }

            double idf = Math.log10((double) totalBooks / booksContainingWord);
            idfScores.putIfAbsent(word, idf);
        }

        return idfScores;
    }

    private double cosineSimilarity(Map<String, Double> first, Map<String, Double> second) {
        double magnitudeFirst = magnitude(first.values());
        double magnitudeSecond = magnitude(second.values());

        return dotProduct(first, second) / (magnitudeFirst * magnitudeSecond);
    }

    private double dotProduct(Map<String, Double> first, Map<String, Double> second) {
        Set<String> commonKeys = new HashSet<>(first.keySet());
        commonKeys.retainAll(second.keySet());

        return commonKeys.stream()
                .mapToDouble(word -> first.get(word) * second.get(word))
                .sum();
    }

    private double magnitude(Collection<Double> input) {
        double squaredMagnitude = input.stream()
                .map(v -> v * v)
                .reduce(0.0, Double::sum);

        return Math.sqrt(squaredMagnitude);
    }
}

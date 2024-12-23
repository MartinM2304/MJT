package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class BookRecommender implements BookRecommenderAPI {

    private static final double RECOMMEND_BOOK_MULTIPLIER = -1.0;
    private static final int MINIMUM_MAXN = 0;
    private Set<Book> books;
    private SimilarityCalculator similarityCalculator;

    public BookRecommender(Set<Book> initialBooks, SimilarityCalculator calculator) {
        if (initialBooks == null || initialBooks.isEmpty()) {
            throw new IllegalArgumentException("Book set cannot be null or empty");
        }
        if (calculator == null) {
            throw new IllegalArgumentException("Similarity calculator cannot be null");
        }

        this.books = initialBooks;
        this.similarityCalculator = calculator;
    }

    /**
     * Searches for books that are similar to the provided one.
     *
     * @param origin the book we should calculate similarity with.
     * @param maxN   the maximum number of entries returned
     * @return a SortedMap<Book, Double> representing the top maxN closest books
     * with their similarity to originBook ordered by their similarity score
     * @throws IllegalArgumentException if the originBook is null.
     * @throws IllegalArgumentException if maxN is smaller or equal to 0.
     */
    @Override
    public SortedMap<Book, Double> recommendBooks(Book origin, int maxN) {
        if (origin == null || maxN <= MINIMUM_MAXN) {
            throw new IllegalArgumentException("book is null or maxN is smaller or equal to 0");
        }

        SortedMap<Book, Double> result = new TreeMap<>(Comparator.comparingDouble((Book b) ->
                        RECOMMEND_BOOK_MULTIPLIER * similarityCalculator.calculateSimilarity(origin, b))
                .thenComparing(Book::title));

        for (Book book : books) {
            if (book.equals(origin)) {
                continue;
            }
            double similarity = similarityCalculator.calculateSimilarity(origin, book);

            if (result.size() >= maxN) {
                Book lastBook = result.lastKey();
                double lastSimilarity = result.get(lastBook);
                if (lastSimilarity < similarity) {
                    result.remove(lastBook);
                    result.put(book, similarity);
                }
            } else {
                result.put(book, similarity);
            }
        }
        return result;
    }

}

package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenresOverlapSimilarityCalculatorTest {

    @Test
    void testCalculateSimilarityWithSameGenres() {
        SimilarityCalculator calculator = new GenresOverlapSimilarityCalculator();
        Book book1 = new Book("1", "Book 1", "Author A", "Descr b1, Hello", List.of("Fantasy", "Adventure"),
                4.5, 1200, "http://book1.com");
        Book book2 = new Book("2", "Book 2", "Author B", "Descr b2 Hello Hello", List.of("Fantasy", "Adventure"),
                4.2, 850, "http://book2.com");
        double similarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(1.0, similarity,
                "similarity should be 1");
    }

    @Test
    void testCalculateSimilarityDifferent() {
        SimilarityCalculator calculator = new GenresOverlapSimilarityCalculator();
        Book book1 = new Book("1", "Book 1", "Author A", "Descr b1, Hello", List.of("Fantasy", "Adventure"),
                4.5, 1200, "http://book1.com");
        Book book2 = new Book("2", "Book 2", "Author B", "Descr b2 Hello Hello", List.of("Nothing"),
                4.2, 850, "http://book2.com");

        double similarity = calculator.calculateSimilarity(book1, book2);

        assertEquals(0, similarity, "similarity should be 0");
    }

    @Test
    void testCalculateSimilarityWithNull() {
        SimilarityCalculator calculator = new GenresOverlapSimilarityCalculator();
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(null, null),
                "cant calculate similarity with null");
    }
}
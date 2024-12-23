package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CompositeSimilarityCalculatorTest {

    @Test
    void testCalculateSimilarity() {
        SimilarityCalculator calc1 = mock(SimilarityCalculator.class);
        SimilarityCalculator calc2 = mock(SimilarityCalculator.class);

        Book book1 = new Book("1", "Book 1", "Author A", "Descr b1, Hello", List.of("Fantasy", "Adventure"), 4.5, 1200, "http://book1.com");
        Book book2 = new Book("2", "Book 2", "Author B", "Descr b2 Hello Hello", List.of("Science Fiction"), 4.2, 850, "http://book2.com");

        when(calc1.calculateSimilarity(book1, book2)).thenReturn(0.7);
        when(calc2.calculateSimilarity(book1, book2)).thenReturn(0.3);

        SimilarityCalculator compositeCalculator =
                new CompositeSimilarityCalculator(Map.of(calc1, 0.6, calc2, 0.4));

        double similarity = compositeCalculator.calculateSimilarity(book1, book2);

        assertEquals(0.54, similarity,"Similarity should be equal");
    }

    @Test
    void testConstrWithNull(){
        assertThrows(IllegalArgumentException.class,()->new CompositeSimilarityCalculator(null)
                ,"Cant pass null to the constructor");
    }

    @Test
    void testCalculateSimilarityWithNull(){
        SimilarityCalculator calc1 = mock(SimilarityCalculator.class);
        SimilarityCalculator calc2 = mock(SimilarityCalculator.class);

        Book book1 = new Book("1", "Book 1", "Author A", "Descr b1, Hello", List.of("Fantasy", "Adventure"), 4.5, 1200, "http://book1.com");
        Book book2 = new Book("2", "Book 2", "Author B", "Descr b2 Hello Hello", List.of("Science Fiction"), 4.2, 850, "http://book2.com");

        when(calc1.calculateSimilarity(book1, book2)).thenReturn(0.7);
        when(calc2.calculateSimilarity(book1, book2)).thenReturn(0.3);

        SimilarityCalculator compositeCalculator =
                new CompositeSimilarityCalculator(Map.of(calc1, 0.6, calc2, 0.4));
        assertThrows(IllegalArgumentException.class,()->compositeCalculator.calculateSimilarity(null,null)
                ,"calculateSimilarity must throw exception for null");
    }
}
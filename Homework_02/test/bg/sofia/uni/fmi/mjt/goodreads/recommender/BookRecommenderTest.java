package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookRecommenderTest {
    private SimilarityCalculator similarityCalculator;
    private BookRecommender bookRecommender;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        similarityCalculator = mock();
        book1 = new Book("1", "Book 1", "Author A", "Descr b1, Hello", List.of("Fantasy", "Adventure"), 4.5, 1200, "http://book1.com");
        book2 = new Book("2", "Book 2", "Author B", "Descr b2 Hello Hello", List.of("Science Fiction"), 4.2, 850, "http://book2.com");
        book3 = new Book("3", "Book 3", "Author A", "Descr b3 Hello", List.of("Fantasy", "Drama"), 3.9, 400, "http://book3.com");

        bookRecommender = new BookRecommender(Set.of(book1, book2, book3), similarityCalculator);
    }

    @Test
    void testRecommendBooksValidInput() {
        when(similarityCalculator.calculateSimilarity(book1, book2)).thenReturn(0.8);
        when(similarityCalculator.calculateSimilarity(book1, book3)).thenReturn(0.5);

        SortedMap<Book, Double> recommendations = bookRecommender.recommendBooks(book1, 2);

        assertEquals(2, recommendations.size(),"size should be 2");
        assertTrue(recommendations.containsKey(book2),"book2 should be recommended");
        assertTrue(recommendations.containsKey(book3),"book3 should be recommended");
    }

    @Test
    void testRecommendBooksNullBook() {
        assertThrows(IllegalArgumentException.class, () -> bookRecommender.recommendBooks(null, 2),
                "Cant pass null for origin");
    }

    @Test
    void testRecommendBooksInvalidMaxN() {
        assertThrows(IllegalArgumentException.class, () -> bookRecommender.recommendBooks(book1, 0),
                "cant pass 0 for maxN");
    }

    @Test
    void createRecommenderWithNullBooks(){
        assertThrows(IllegalArgumentException.class,()->new BookRecommender(null,null),
                "cant create recoommende with null books");
    }

    @Test
    void createRecommenderWithNullCalculator(){
        assertThrows(IllegalArgumentException.class,()->new BookRecommender(Set.of(book1,book2,book3),null),
                "cant create recoommende with null calculator");
    }

    @Test
    void testRecommendBooksSmallMax() {
        when(similarityCalculator.calculateSimilarity(book1, book2)).thenReturn(0.8);
        when(similarityCalculator.calculateSimilarity(book1, book3)).thenReturn(0.5);

        SortedMap<Book, Double> recommendations = bookRecommender.recommendBooks(book1, 1);

        assertEquals(1, recommendations.size(),"size should be 1");
        assertTrue(recommendations.containsKey(book2),"book2 should be recommended");
        assertFalse(recommendations.containsKey(book3),"book3 should not be recommended");
    }

}
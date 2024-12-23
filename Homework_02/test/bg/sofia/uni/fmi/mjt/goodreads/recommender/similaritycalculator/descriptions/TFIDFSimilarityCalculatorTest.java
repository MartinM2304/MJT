package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TFIDFSimilarityCalculatorTest {

    private TFIDFSimilarityCalculator calculator;
    private TextTokenizer tokenizer;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        tokenizer = mock(TextTokenizer.class);
        book1 = new Book("1", "Book 1", "Author A", "Descr b1, Hello", List.of("Fantasy", "Adventure"),
                4.5, 1200, "http://book1.com");
        book2 = new Book("2", "Book 2", "Author B", "Descr b2 Hello Hello", List.of("Science Fiction"),
                4.2, 850, "http://book2.com");

        calculator = new TFIDFSimilarityCalculator(Set.of(book1, book2), tokenizer);
    }

    @Test
    void testComputeTF() {
        when(tokenizer.tokenize(book1.description())).thenReturn(List.of("word1", "word2", "word1"));
        Map<String, Double> tf = calculator.computeTF(book1);
        assertEquals(1.0 / 3, tf.get("word2"),0.01,"the idf of word2 should be 1/3");
    }


    @Test
    void testComputeIDF() {
        when(tokenizer.tokenize(book1.description())).thenReturn(List.of("word1", "word2"));
        when(tokenizer.tokenize(book2.description())).thenReturn(List.of("word2", "word3"));

        Map<String, Double> idf = calculator.computeIDF(book1);
        assertTrue(idf.get("word1") > idf.get("word2"),
                "idf of word1 is bigger than idf of word2");
    }

    @Test
    void testTFWithNull(){
        assertThrows(IllegalArgumentException.class,()->calculator.computeIDF(null),
                "cant create calculator with null");
    }

    @Test
    void testCalculateSimilarityValidBooks() {
        when(tokenizer.tokenize(book1.description())).thenReturn(List.of("great", "adventure", "story"));
        when(tokenizer.tokenize(book2.description())).thenReturn(List.of("epic", "fantasy", "saga"));

        double similarity = calculator.calculateSimilarity(book1, book2);

        assertTrue(similarity >= 0 && similarity <= 1, "Similarity should be between 0 and 1");
    }

    @Test
    void testComputeTFIDFValidBook() {

        when(tokenizer.tokenize(book1.description())).thenReturn(List.of("great", "adventure", "story", "adventure"));
        when(tokenizer.tokenize("An epic fantasy saga")).thenReturn(List.of("epic", "fantasy", "saga"));

        Map<String, Double> tfidf = calculator.computeTFIDF(book1);

        assertEquals(3, tfidf.size(), "size must be 3");
        assertTrue(tfidf.get("adventure") > 0, "tf idf of adventure should be more than 0");
    }
}
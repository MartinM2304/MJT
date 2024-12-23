package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookFinderTest {

    private BookFinder bookFinder;

    @BeforeEach
    void setUp() {
        Set<Book> books = Set.of(
                new Book("1", "Book 1", "Author A", "Descr b1, Hello", List.of("Fantasy", "Adventure"), 4.5, 1200, "http://book1.com"),
                new Book("2", "Book 2", "Author B", "Descr b2 Hello Hello", List.of("Science Fiction"), 4.2, 850, "http://book2.com"),
                new Book("3", "Book 3", "Author A", "Descr b3 Hello", List.of("Fantasy", "Drama"), 3.9, 400, "http://book3.com")
        );

        String stopwords = "and the about of";
        TextTokenizer tokenizer = new TextTokenizer(new StringReader(stopwords));
        bookFinder = new BookFinder(books, tokenizer);
    }


    @Test
    void testSearchByAuthor() {
        List<Book> result = bookFinder.searchByAuthor("Author A");
        assertEquals(2, result.size(), "size should be 2");
    }

    @Test
    void testSearchAuthorNotContaining() {
        List<Book> result = bookFinder.searchByAuthor("Author C");
        assertTrue(result.isEmpty(), "result should be empty");
    }

    @Test
    void testSearchByGenresAll() {
        Set<String> genres = Set.of("Fantasy", "Adventure");
        List<Book> result = bookFinder.searchByGenres(genres, MatchOption.MATCH_ALL);
        assertEquals(1, result.size(), "size should be 1");
        assertEquals("Book 1", result.get(0).title(), "Book 1 should be the matched");
    }

    @Test
    void testSearchByGenresAny() {
        Set<String> genres = Set.of("Fantasy", "Science Fiction");
        List<Book> result = bookFinder.searchByGenres(genres, MatchOption.MATCH_ANY);
        assertEquals(3, result.size(), "size should be 3");
    }

    @Test
    void testSearchByKeywordsAll() {
        Set<String> keywords = Set.of("descr", "book");
        List<Book> result = bookFinder.searchByKeywords(keywords, MatchOption.MATCH_ALL);
        assertEquals(3, result.size(), "size should be 3");
    }

    @Test
    void testSearchByKeywordsAny() {
        Set<String> keywords = Set.of("descr", "book");
        List<Book> result = bookFinder.searchByKeywords(keywords, MatchOption.MATCH_ANY);
        assertEquals(3, result.size(), "size should be 3");
    }

    @Test
    void testAllBooks() {
        Set<Book> allBooks = bookFinder.allBooks();
        assertEquals(3, allBooks.size(), "size should be 3");
    }

    @Test
    void testAllGenres() {
        Set<String> allGenres = bookFinder.allGenres();
        assertEquals(4, allGenres.size(), "size should be 3");
        assertTrue(allGenres.contains("Fantasy"), "Fantasy should be in all genres");
    }

    @Test
    void testSearchByAuthorWithNull() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByAuthor(null), "Cant pass null");
    }

    @Test
    void testSearchByGenreWithNull() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByGenres(null, MatchOption.MATCH_ALL), "Cant pass null");
    }

    @Test
    void testSearchByKeyWordsWithNull() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByKeywords(null, MatchOption.MATCH_ALL), "Cant pass null");
    }

    @Test
    void testValidationForNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> new BookFinder(null, null));
    }
}

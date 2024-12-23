package bg.sofia.uni.fmi.mjt.goodreads.book;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testValidTokens() {
        String[] tokens = {"1", "Book Title", "Author", "Description", "[Fantasy,Adventure]", "4.5", "1000", "http://example.com"};
        assertDoesNotThrow(() -> Book.of(tokens),"Valid tokens should not throw exception");
    }

    @Test
    void testInvalidNumberOfTokens() {
        String[] tokens = {"1", "Book Title", "Author", "Description", "[Fantasy,Adventure]", "4.5", "1000"};
        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens),"Invalid number of tokens");
    }

    @Test
    void testNullTokensArray() {
        assertThrows(IllegalArgumentException.class, () -> Book.of(null),"Token array cannot be null");
    }

    @Test
    void testNullElementInTokens() {
        String[] tokens = {"1", null, "Author", "Description", "[Fantasy,Adventure]", "4.5", "1000", "http://example.com"};
        assertThrows(IllegalArgumentException.class, () -> Book.of(tokens),"Element cant be null");
    }

    @Test
    void validateInvalidStringToken() {
        assertThrows(IllegalArgumentException.class, () ->new Book(null,null,null,null,null,
                -1,-1,null),"Cant have null string");
    }
    @Test
    void validateInvalidGenresToken() {
        assertThrows(IllegalArgumentException.class, () ->new Book("tmp","tmp","tmp","tmp",
                null,-1,-1,"null"),"cant have null genres");
    }

    @Test
    void validateInvalidRating(){
        List<String>genres=new LinkedList<>();
        genres.add("Fantasy");
        assertThrows(IllegalArgumentException.class, () ->new Book("tmp","tmp","tmp","tmp", genres,
                -1,-1,"null"),"rating cant be -1");
    }

    @Test
    void validateInvalidRatingCountTokens() {
        List<String>genres=new LinkedList<>();
        genres.add("Fantasy");
        assertThrows(IllegalArgumentException.class, () ->new Book("tmp","tmp","tmp","tmp", genres,
                2,-1,"null"),"ratingCount cant be -1");
    }
}

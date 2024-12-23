package bg.sofia.uni.fmi.mjt.goodreads;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookLoaderTest {

    @Test
    void testLoadValidCsv() {
        String csvData = """
                id,title,author,description,genres,rating,reviews,url
                1,Book 1,Author A,"Descr b1, Hello","[Fantasy,Adventure]",4.5,1200,http://book1.com
                2,Book 2,Author B,"Descr b2, World","[Sci-Fi,Thriller]",4.0,800,http://book2.com
                """;
        Reader reader = new StringReader(csvData);
        Set<Book> books = BookLoader.load(reader);

        Book book1 = new Book("1", "Book 1", "Author A", "Descr b1, Hello",
                List.of("Fantasy", "Adventure"), 4.5, 1200, "http://book1.com");
        Book book2 = new Book("2", "Book 2", "Author B", "Descr b2, World",
                List.of("Sci-Fi", "Thriller"), 4.0, 800, "http://book2.com");

        assertTrue(books.contains(book1), "The set should contain Book 1.");
        assertTrue(books.contains(book2), "The set should contain Book 2.");
    }
}
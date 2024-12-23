package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * BookCache is an api to optimize the work with genres and authors
 * as they are one of the common search patterns, we save what are the
 * probable books based on genres and authors
 */
public class BookCache implements BookCacheAPI {

    private Map<String, Set<Book>> booksByGenre = new HashMap<>();
    private Map<String, Set<Book>> booksByAuthor = new HashMap<>();

    public BookCache(Set<Book> books) {
        for (Book book : books) {
            updateAuthor(book);
            updateGenres(book);
        }
    }

    private void updateGenres(Book book) {
        List<String> genres = book.genres();

        for (String genre : genres) {
            String normalizedGenre = genre.replaceAll("^['\"]|['\"]$", "").trim();
            if (!booksByGenre.containsKey(normalizedGenre)) {
                Set<Book> bookSet = new HashSet<>();
                bookSet.add(book);
                booksByGenre.put(normalizedGenre, bookSet);
            } else {
                booksByGenre.get(normalizedGenre).add(book);
            }
        }
    }

    private void updateAuthor(Book book) {
        String author = book.author();
        if (!booksByAuthor.containsKey(author)) {
            Set<Book> bookSet = new HashSet<>();
            bookSet.add(book);
            booksByAuthor.put(author, bookSet);
        } else {
            booksByAuthor.get(author).add(book);
        }
    }

    private void validateString(String str) {
        if (str == null || str.isEmpty() || str.isBlank()) {
            throw new IllegalArgumentException("author or genre is not valid");
        }
    }

    @Override
    public Set<Book> getBooksByAuthor(String author) {
        validateString(author);
        if (!booksByAuthor.containsKey(author)) {
            return Collections.emptySet();
        }
        return booksByAuthor.get(author);
    }

    @Override
    public Set<String> allGenres() {
        return booksByGenre.keySet();
    }

    @Override
    public Set<Book> getBookByGenre(String genre) {
        validateString(genre);
        String normalizedGenre = genre.replaceAll("^['\"]|['\"]$", "").trim();
        return booksByGenre.getOrDefault(normalizedGenre, Collections.emptySet());
    }
}

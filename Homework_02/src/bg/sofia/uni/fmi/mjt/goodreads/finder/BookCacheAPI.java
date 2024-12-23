package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;

import java.util.Set;

/*
 * BookCache is an api to optimize the work with genres and authors
 * as they are one of the common search patterns, we save what are the
 * probable books based on genres and authors
 */
public interface BookCacheAPI {

    Set<Book> getBooksByAuthor(String author);

    Set<String> allGenres();

    Set<Book> getBookByGenre(String genre);
}

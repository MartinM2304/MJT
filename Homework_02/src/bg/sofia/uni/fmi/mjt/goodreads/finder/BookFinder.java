package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BookFinder implements BookFinderAPI {

    private final Set<Book> books;
    private final TextTokenizer tokenizer;
    private final BookCacheAPI bookCache;

    public BookFinder(Set<Book> books, TextTokenizer tokenizer) {
        if (books == null || tokenizer == null) {
            throw new IllegalArgumentException("books or tokenizer is null");
        }

        this.books = books;
        bookCache = new BookCache(this.books);
        this.tokenizer = tokenizer;
    }

    private void validateString(String str) {
        if (str == null || str.isEmpty() || str.isBlank()) {
            throw new IllegalArgumentException("author or genre is not valid");
        }
    }

    private List<Book> searchByGenresAll(Set<String> genres) {
        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Genres set cannot be null or empty");
        }
        String firstGenre = genres.iterator().next();
        Set<Book> booksByGenre = bookCache.getBookByGenre(firstGenre);
        List<Book> result = new LinkedList<>();
        for (Book book : booksByGenre) {
            boolean matchesAll = true;
            for (String genre : book.genres()) {
                String normalizedBookGenre = genre.replaceAll("^['\"]|['\"]$", "").trim();
                if (!genres.contains(normalizedBookGenre)) {
                    matchesAll = false;
                    break;
                }
            }
            if (matchesAll) {
                result.add(book);
            }
        }
        return result;
    }

    private List<Book> searchByGenresAny(Set<String> genres) {
        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Genres set cannot be null or empty");
        }
        List<Book> result = new LinkedList<>();

        for (String genre : genres) {
            Set<Book> bookSet = bookCache.getBookByGenre(genre);
            result.addAll(bookSet);
        }

        return result;
    }

    private List<Book> searchByKeyWordsAny(Set<String> keywords) {
        if (keywords == null) {
            throw new IllegalArgumentException("keywords is null");
        }
        List<Book> result = new LinkedList<>();
        for (Book book : books) {
            boolean matchesAny = false;
            Set<String> title = new HashSet<>(tokenizer.tokenize(book.title()));
            Set<String> description = new HashSet<>(tokenizer.tokenize(book.description()));

            for (String word : keywords) {
                if (title.contains(word) || description.contains(word)) {
                    matchesAny = true;
                    break;
                }
            }
            if (matchesAny) {
                result.add(book);
            }
        }
        return result;
    }

    private List<Book> searchByKeyWordsAll(Set<String> keywords) {
        if (keywords == null) {
            throw new IllegalArgumentException("keywords is null");
        }
        List<Book> result = new LinkedList<>();
        for (Book book : books) {
            boolean matchesAll = true;
            Set<String> title = new HashSet<>(tokenizer.tokenize(book.title()));
            Set<String> description = new HashSet<>(tokenizer.tokenize(book.description()));

            for (String word : keywords) {
                if (!title.contains(word) && !description.contains(word)) {
                    matchesAll = false;
                    break;
                }
            }
            if (matchesAll) {
                result.add(book);
            }
        }
        return result;
    }

    public Set<Book> allBooks() {
        return books;
    }

    @Override
    public List<Book> searchByAuthor(String authorName) {
        validateString(authorName);
        List<Book> result = new LinkedList<>(bookCache.getBooksByAuthor(authorName));
        return result;
    }

    @Override
    public Set<String> allGenres() {
        return bookCache.allGenres();
    }

    @Override
    public List<Book> searchByGenres(Set<String> genres, MatchOption option) {
        if (genres == null) {
            throw new IllegalArgumentException("genres is null");
        }
        if (option == MatchOption.MATCH_ALL) {
            return searchByGenresAll(genres);
        } else {
            return searchByGenresAny(genres);
        }
    }

    @Override
    public List<Book> searchByKeywords(Set<String> keywords, MatchOption option) {
        if (keywords == null) {
            throw new IllegalArgumentException("genres is null");
        }
        if (option == MatchOption.MATCH_ALL) {
            return searchByKeyWordsAll(keywords);
        } else {
            return searchByKeyWordsAny(keywords);
        }
    }

}

package bg.sofia.uni.fmi.mjt.goodreads.book;

import bg.sofia.uni.fmi.mjt.goodreads.book.utill.genres.GenresWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record Book(
        String ID,
        String title,
        String author,
        String description,
        List<String> genres,
        double rating,
        int ratingCount,
        String URL
) {


    private static final int TOKENS_COUNT = 8;
    private static final int ID_INDEX = 0;
    private static final int TITLE_INDEX = 1;
    private static final int AUTHOR_INDEX = 2;
    private static final int DESCRIPTION_INDEX = 3;
    private static final int GENRES_INDEX = 4;
    private static final int RATING_INDEX = 5;
    private static final int RATING_COUNT_INDEX = 6;
    private static final int URL_INDEX = 7;
    private static final int MAXIMUM_RATING = 5;
    private static final int MINIMUM_RATING = 0;
    private static final int MINIMUM_RATING_COUNT = 0;

    private static void validateTokens(String[] tokens) {
        if (tokens == null) {
            throw new IllegalArgumentException("Tokens array cannot be null");
        }

        if (tokens.length != TOKENS_COUNT) {
            throw new IllegalArgumentException("Invalid number of tokens, expected: " +
                    TOKENS_COUNT + ", got: " + tokens.length);
        }

        if (Arrays.stream(tokens).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Tokens array contains null elements");
        }

    }

    private void validateString(String str) {
        if (str == null) {
            throw new IllegalArgumentException("String cannot be null or blank");
        }
    }

    public Book {
        validateString(ID);
        validateString(title);
        validateString(author);
        validateString(description);
        validateString(URL);
        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Genres cannot be null or empty");
        }
        if (rating < MINIMUM_RATING || rating > MAXIMUM_RATING) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
        if (ratingCount < MINIMUM_RATING_COUNT) {
            throw new IllegalArgumentException("Rating count cannot be negative");
        }

    }

    public static Book of(String[] tokens) {
        validateTokens(tokens);
        String id = tokens[ID_INDEX].trim();
        String title = tokens[TITLE_INDEX].trim();
        String author = tokens[AUTHOR_INDEX].trim();
        String description = tokens[DESCRIPTION_INDEX].trim();
        String genresToken = tokens[GENRES_INDEX].trim();
        List<String> genres = GenresWrapper.splitGenresToken(genresToken);
        double avgRating = Double.parseDouble(tokens[RATING_INDEX].trim());
        int numRatings = Integer.parseInt(tokens[RATING_COUNT_INDEX].trim().replace(",", ""));
        String url = tokens[URL_INDEX].trim();

        return new Book(id, title, author, description, genres, avgRating, numRatings, url);

    }
}

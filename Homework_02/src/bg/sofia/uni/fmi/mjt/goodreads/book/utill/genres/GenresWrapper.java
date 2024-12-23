package bg.sofia.uni.fmi.mjt.goodreads.book.utill.genres;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.math.NumberUtils.min;

public class GenresWrapper {

    private static final double GENRES_EMPTY_RETURN_VALUE = 0.0;

    private static void validateToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or blank");
        }

        if ((!token.startsWith("[") || !token.endsWith("]")) &&
                (!token.startsWith("{") || !token.endsWith("}"))) {
            throw new IllegalArgumentException("Token must be enclosed in square brackets");
        }
    }

    private static SplitGenreConfig getSplitter() {
        return SplitGenreConfig.SQUARE_BRACKETS;
    }

    /*
     *added for better abstraction of code, only variant implemented is [genre,genre]
     */
    public static List<String> splitGenresToken(String token) {
        validateToken(token);
        SplitGenreConfig splitter = getSplitter();

        GenresTokenSplitterAPI splitterAPI = switch (splitter) {
            case SQUARE_BRACKETS -> new GenresSplitterBySquareBracket(token);
            case CURLY_BRACKETS -> new GenresSplitterByCurlyBracket(token);
        };

        return splitterAPI.splitGenre();
    }

    public static double overlapCoefficient(Set<String> genresFirst, Set<String> genresSecond) {
        if (genresFirst == null || genresSecond == null) {
            throw new IllegalArgumentException("Genres sets cannot be null");
        }
        if (genresFirst.isEmpty() || genresSecond.isEmpty()) {
            return GENRES_EMPTY_RETURN_VALUE;
        }

        Set<String> intersection = new HashSet<>(genresFirst);
        intersection.retainAll(genresSecond);
        double min = min(genresFirst.size(), genresSecond.size());
        return (intersection.size() / min);
    }
}

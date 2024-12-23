package bg.sofia.uni.fmi.mjt.goodreads.book.utill.genres;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenresSplitterByCurlyBracket implements GenresTokenSplitterAPI {

    private String token;

    public GenresSplitterByCurlyBracket(String genres) {
        validateGenres(genres);
        this.token = genres;
    }

    private void validateGenres(String genres) {
        if (genres == null || genres.isEmpty() || genres.isBlank()) {
            throw new IllegalArgumentException("genres is not valid");
        }

        if (!genres.startsWith("{") || !genres.endsWith("}")) {
            throw new IllegalArgumentException("Genres must be enclosed in curly brackets");
        }
    }

    @Override
    public List<String> splitGenre() {
        List<String> genres = Arrays.stream(token.replace("{", "").replace("}s", "").split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        return genres;
    }
}

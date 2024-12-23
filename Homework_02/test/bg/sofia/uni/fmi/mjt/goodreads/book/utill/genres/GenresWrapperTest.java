package bg.sofia.uni.fmi.mjt.goodreads.book.utill.genres;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GenresWrapperTest {

    @Test
    void testSplitGenresInvalid() {
        String token = "[Fantasy, Adventure, Mystery]";
        List<String> genres = GenresWrapper.splitGenresToken(token);
        assertEquals(3, genres.size(),"size should be 3");
        assertTrue(genres.contains("Fantasy"),"Fantasy should be in genres");
        assertTrue(genres.contains("Adventure"),"Adventure should be in genres");
    }

    @Test
    void testSplitGenresMisingBracket() {
        String token = "Fantasy, Adventure, Mystery";
        assertThrows(IllegalArgumentException.class, () -> GenresWrapper.splitGenresToken(token),"cant split invalid token");
    }

    @Test
    void testOverlapCoefficient() {
        Set<String> genresFirst = Set.of("Fantasy", "Adventure");
        Set<String> genresSecond = Set.of("Adventure", "Drama");

        double overlap = GenresWrapper.overlapCoefficient(genresFirst, genresSecond);
        assertEquals(0.5, overlap,"overlap should be 0.5");
    }

    @Test
    void testOverlapCoefficientInvalid() {
        double overlap = GenresWrapper.overlapCoefficient(Collections.emptySet(), Collections.emptySet());
        assertEquals(0.0, overlap,"overlap should be 0");
    }

    @Test
    void testOverlapWithNull() {
        assertThrows(IllegalArgumentException.class, () -> GenresWrapper.overlapCoefficient(null, null),
                "cant overlap with null books");
    }

}

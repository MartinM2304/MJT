package bg.sofia.uni.fmi.mjt.goodreads.book.utill.genres;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenresSplitterByCurlyBracketTest  {

    @Test
    void testSplitGenreValid() {
        GenresSplitterByCurlyBracket splitter = new GenresSplitterByCurlyBracket("{Fantasy, Adventure, Mystery}");
        List<String> genres = splitter.splitGenre();
        assertEquals(3, genres.size(),"genres size should be 3");
        assertTrue(genres.contains("Fantasy"),"Fantasy should be in genres");
    }

    @Test
    void testSplitGenreInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new GenresSplitterByCurlyBracket("Fantasy, Adventure"),"Cant pass invalid genres");
    }
}
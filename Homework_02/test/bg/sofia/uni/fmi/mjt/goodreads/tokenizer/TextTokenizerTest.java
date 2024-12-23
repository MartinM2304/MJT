package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TextTokenizerTest {

    @Test
    void testTextTokenizerNull(){
        assertThrows(IllegalArgumentException.class,()->new TextTokenizer(null),
                "cant create tokenizer with null");
    }

    @Test
    void testTokenizerNull(){
        assertThrows(IllegalArgumentException.class,()->new TextTokenizer(null),
                "cant create tokenizer with null");
    }

    @Test
    void testStopWords(){
        Reader input=new StringReader("hello\nword");
        TextTokenizer tokenizer=new TextTokenizer(input);
        assertEquals(Set.of("hello","word"),tokenizer.stopwords(),
                "stopwords should be the same");
    }

    @Test
    void testTokenizeNull(){
        Reader input=new StringReader("hello\nword");
        TextTokenizer tokenizer=new TextTokenizer(input);
        assertThrows(IllegalArgumentException.class,()->tokenizer.tokenize(null),
                "cant create tokenizer with null");
    }

}
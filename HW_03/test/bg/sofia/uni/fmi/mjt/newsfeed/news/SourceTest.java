package bg.sofia.uni.fmi.mjt.newsfeed.news;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SourceTest {
    @Test
    public void testSourceWithValidInput() {
        Source source = new Source("id", "name");
        assertNotNull(source, "Source should be created successfully");
        assertEquals("id", source.id(), "ID should match");
        assertEquals("name", source.name(), "Name should match");
    }

}
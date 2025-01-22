package bg.sofia.uni.fmi.mjt.newsfeed.request;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.IllegalRequestParameter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NewsFeedRequestTest {

    @Test
    public void testBuildRequestSuccessfully() {
        NewsFeedRequest request = NewsFeedRequest.getBuilder("apiKey")
                .addKeyWords(List.of("test"))
                .addCategories(List.of("category"))
                .addCountries(List.of("country"))
                .setPage(1)
                .setPageSize(10)
                .build();

        assertTrue(request.build().contains("apiKey=apiKey"), "API key should be included in the built request");
    }

    @Test
    public void testBuildRequestWithoutKeywords() {
        assertThrows(IllegalRequestParameter.class, () ->
                        NewsFeedRequest.getBuilder("apiKey").build(),
                "Should throw exception when keywords are missing");
    }


    @Test
    public void testUpdatePageWithInvalidPage() {
        NewsFeedRequest request = NewsFeedRequest.getBuilder("apiKey")
                .addKeyWords(List.of("test"))
                .build();

        assertThrows(IllegalArgumentException.class, () -> request.updatePage(-1),
                "Should throw exception for invalid page number");
    }

    @Test
    public void testSetPageSizeOutOfBounds() {
        NewsFeedRequest.NewsFeedBuilder builder = NewsFeedRequest.getBuilder("apiKey");

        assertThrows(IllegalArgumentException.class, () -> builder.setPageSize(0),
                "Should throw exception for page size < 1");
        assertThrows(IllegalArgumentException.class, () -> builder.setPageSize(101),
                "Should throw exception for page size > 100");
    }

    @Test
    public void testAddNullKeywords() {
        NewsFeedRequest.NewsFeedBuilder builder = NewsFeedRequest.getBuilder("apiKey");

        assertThrows(IllegalArgumentException.class, () -> builder.addKeyWords(null),
                "Should throw exception for null keywords");
    }

    @Test
    public void testAddNullCategories() {
        NewsFeedRequest.NewsFeedBuilder builder = NewsFeedRequest.getBuilder("apiKey");

        assertThrows(IllegalArgumentException.class, () -> builder.addCategories(null),
                "Should throw exception for null categories");
    }

    @Test
    public void testAddNullCountries() {
        NewsFeedRequest.NewsFeedBuilder builder = NewsFeedRequest.getBuilder("apiKey");

        assertThrows(IllegalArgumentException.class, () -> builder.addCountries(null),
                "Should throw exception for null countries");
    }

    @Test
    public void testSetPageWithInvalid(){
        assertThrows(IllegalArgumentException.class,()->NewsFeedRequest.getBuilder("apiKey")
                .addKeyWords(List.of("test"))
                .addCategories(List.of("category"))
                .addCountries(List.of("country"))
                .setPage(0)
                .setPageSize(10)
                .build(),
                "Should throw page cant be smaller than 1");
    }
}
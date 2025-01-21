package bg.sofia.uni.fmi.mjt.newsfeed.client;


import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsApiErrorException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ResponseIncorrectException;
import bg.sofia.uni.fmi.mjt.newsfeed.news.Article;
import bg.sofia.uni.fmi.mjt.newsfeed.response.NewsFeedResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NewsFeedClientTest {

    @Test
    public void testValidStatus() {
        String validResponseJson = """
            {
                "status": "ok",
                "articles": [{"title": "Title1", "content": "Content1"}]
            }
            """;

        NewsFeedResponse response = new NewsFeedResponse(validResponseJson);

        assertEquals("ok", response.getStatus(),"The status should be ok");
    }

    @Test
    public void testValidResponse(){
        String validResponseJson = """
            {
                "status": "ok",
                "articles": [{"title": "Title1", "content": "Content1"}]
            }
            """;

        NewsFeedResponse response = new NewsFeedResponse(validResponseJson);

        List<Article> articles = response.getData();
        assertEquals("Title1", articles.get(0).getTitle(),"Title should be Title1");
    }

    @Test
    public void testResponseMissingStatus() {
        String invalidResponseJson = """
            {
                "articles": []
            }
            """;

        assertThrows(ResponseIncorrectException.class, () -> new NewsFeedResponse(invalidResponseJson),"ResponseIncorrectException should be thrown");
    }

    @Test
    public void testResponseErrorStatus() {
        String errorResponseJson = """
            {
                "status": "error",
                "code": "apiKeyInvalid",
                "message": "Your API key is invalid"
            }
            """;

        assertThrows(NewsApiErrorException.class, () -> new NewsFeedResponse(errorResponseJson),"NewsApiErrorException must be thrown");
    }

    @Test
    public void testResponseMissingArticles() {
        String invalidResponseJson = """
            {
                "status": "ok"
            }
            """;

        assertThrows(ResponseIncorrectException.class, () -> new NewsFeedResponse(invalidResponseJson),"ResponseIncorrectException should be thrown");
    }

    @Test
    public void testEmptyArticlesArray() {
        String emptyArticlesJson = """
            {
                "status": "ok",
                "articles": []
            }
            """;

        NewsFeedResponse response = new NewsFeedResponse(emptyArticlesJson);

        assertNotNull(response.getData(),"response should not be null");
        assertTrue(response.getData().isEmpty(),"response should not be null");
    }

    @Test
    public void testIncorrectJson() {
        String malformedJson = "{";

        assertThrows(ResponseIncorrectException.class, () -> new NewsFeedResponse(malformedJson),"JSON must be in valid format");
    }
}
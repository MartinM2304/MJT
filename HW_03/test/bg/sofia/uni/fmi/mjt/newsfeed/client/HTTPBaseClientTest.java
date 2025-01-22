package bg.sofia.uni.fmi.mjt.newsfeed.client;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ClientSendException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.JSONParsingException;
import bg.sofia.uni.fmi.mjt.newsfeed.request.Request;
import bg.sofia.uni.fmi.mjt.newsfeed.response.PagedResponse;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HTTPBaseClientTest {

    private HttpClient mockHttpClient;
    private Request mockRequest;
    private Function<String, PagedResponse<String>> mockParser;
    private HttpResponse<String> mockHttpResponse;
    private HTTPBaseClient<String> client;

    private static final String MOCK_RESPONSE_JSON = """
            {
                "status": "ok",
                "totalResults": 10,
                "articles": ["Article1", "Article2"]
            }
            """;

    @BeforeEach
    public void setUp() {
        mockHttpClient = mock(HttpClient.class);
        mockRequest = mock(Request.class);
        mockParser = mock(Function.class);
        mockHttpResponse = mock(HttpResponse.class);

        when(mockRequest.build()).thenReturn("http://mock.api/newsfeed?page=1&pageSize=2");
        when(mockRequest.pageSize()).thenReturn(2);

        client = new HTTPBaseClient<>(
                new HTTPBaseClient.HTTPBaseClientBuilder<>(
                        mockHttpClient,
                        mockRequest,
                        mockParser
                )
        );
    }

    @Test
    public void testLoadPagesSuccess() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(MOCK_RESPONSE_JSON);

        PagedResponse<String> mockPagedResponse = mock(PagedResponse.class);
        when(mockPagedResponse.getData()).thenReturn(List.of("Article1", "Article2"));
        when(mockParser.apply(MOCK_RESPONSE_JSON)).thenReturn(mockPagedResponse);

        List<String> result = client.loadPages(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Article1"));
        assertTrue(result.contains("Article2"));

        verify(mockRequest).updatePage(1);
        verify(mockParser).apply(MOCK_RESPONSE_JSON);
        verify(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testLoadPagesFinalPageDetection() throws Exception {
        String singlePageResponseJson = """
        {
            "status": "ok",
            "totalResults": 1,
            "articles": [
                {
                    "source": {
                        "id": "the-wall-street-journal",
                        "name": "The Wall Street Journal"
                    },
                    "author": "The Wall Street Journal",
                    "title": "Trump’s Executive Orders Will Focus on the Border, Energy - The Wall Street Journal",
                    "description": null,
                    "url": "https://www.wsj.com/politics/policy/trump-to-lay-out-trade-visionbut-wont-impose-new-tariffs-yet-b9c8378d",
                    "urlToImage": null,
                    "publishedAt": "2025-01-20T17:57:00Z",
                    "content": null
                }
            ]
        }
        """;

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(singlePageResponseJson);

        PagedResponse<String> mockPagedResponse = mock(PagedResponse.class);
        when(mockPagedResponse.getData()).thenReturn(List.of(
                "Trump’s Executive Orders Will Focus on the Border, Energy - The Wall Street Journal"));
        when(mockParser.apply(singlePageResponseJson)).thenReturn(mockPagedResponse);

        List<String> result = client.loadPages(5);

        assertTrue(result.contains("Trump’s Executive Orders Will Focus on the Border, Energy - The Wall Street Journal"));
    }

    @Test
    public void testLoadPagesClientSendException() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Network error"));

        assertThrows(ClientSendException.class, () -> client.loadPages(1));

        verify(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testLoadPagesJSONParsingException() throws Exception {
        String errorResponseJson = """
            {
                "errors": "Some error"
            }
            """;
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(errorResponseJson);

        assertThrows(JSONParsingException.class, () -> client.loadPages(1));
    }

    @Test
    public void testLoadPagesEmptyResults() throws Exception {
        String emptyResponseJson = """
            {
                "status": "ok",
                "totalResults": 0,
                "articles": []
            }
            """;
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(emptyResponseJson);

        PagedResponse<String> mockPagedResponse = mock(PagedResponse.class);
        when(mockPagedResponse.getData()).thenReturn(List.of());
        when(mockParser.apply(emptyResponseJson)).thenReturn(mockPagedResponse);

        List<String> result = client.loadPages(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testBuilderInitializesCorrectly() {
        Request mockRequest = mock(Request.class);
        Function<String, PagedResponse<String>> mockParser = mock(Function.class);

        HTTPBaseClient.HTTPBaseClientBuilder<String> builder =
                new HTTPBaseClient.HTTPBaseClientBuilder<>(mockRequest, mockParser);

        assertNotNull(builder, "Builder should initialize properly.");
    }

    @Test
    public void testSetClient() {
        Request mockRequest = mock(Request.class);
        Function<String, PagedResponse<String>> mockParser = mock(Function.class);
        HttpClient mockHttpClient = mock(HttpClient.class);

        HTTPBaseClient.HTTPBaseClientBuilder<String> builder =
                new HTTPBaseClient.HTTPBaseClientBuilder<>(mockRequest, mockParser);

        builder.setClient(mockHttpClient);

        HTTPBaseClient<String> client = builder.build();

        assertNotNull(client, "Client should be created successfully.");
    }

    @Test
    public void testSetPagesLimit() {
        Request mockRequest = mock(Request.class);
        Function<String, PagedResponse<String>> mockParser = mock(Function.class);

        HTTPBaseClient.HTTPBaseClientBuilder<String> builder =
                new HTTPBaseClient.HTTPBaseClientBuilder<>(mockRequest, mockParser);

        int customPagesLimit = 10;
        builder.setPagesLimit(customPagesLimit);

        HTTPBaseClient<String> client = builder.build();

        assertNotNull(client, "Client should be created successfully.");
    }

    @Test
    public void testSetPagesLimitThrowsExceptionForInvalidLimit() {
        Request mockRequest = mock(Request.class);
        Function<String, PagedResponse<String>> mockParser = mock(Function.class);

        HTTPBaseClient.HTTPBaseClientBuilder<String> builder =
                new HTTPBaseClient.HTTPBaseClientBuilder<>(mockRequest, mockParser);

        assertThrows(IllegalArgumentException.class, () -> builder.setPagesLimit(0),
                "Setting a pages limit less than 1 should throw an exception.");
    }

    @Test
    public void testBuildCreatesClient() throws ClientSendException,InterruptedException,IOException,JSONParsingException{
        String singlePageResponseJson = """
        {
            "status": "ok",
            "totalResults": 1,
            "articles": [
                {
                    "source": {
                        "id": "the-wall-street-journal",
                        "name": "The Wall Street Journal"
                    },
                    "author": "The Wall Street Journal",
                    "title": "Trump’s Executive Orders Will Focus on the Border, Energy - The Wall Street Journal",
                    "description": null,
                    "url": "https://www.wsj.com/politics/policy/trump-to-lay-out-trade-visionbut-wont-impose-new-tariffs-yet-b9c8378d",
                    "urlToImage": null,
                    "publishedAt": "2025-01-20T17:57:00Z",
                    "content": null
                }
            ]
        }
        """;

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(singlePageResponseJson);

        PagedResponse<String> mockPagedResponse = mock(PagedResponse.class);
        when(mockPagedResponse.getData()).thenReturn(List.of(
                "Trump’s Executive Orders Will Focus on the Border, Energy - The Wall Street Journal"));
        when(mockParser.apply(singlePageResponseJson)).thenReturn(mockPagedResponse);

        List<String> result = client.loadAll();
        assertNotNull(result,"result should not be null");
    }

    @Test
    public void testExecuteWithInvalidPageSize() throws Exception {
        String mockResponse = """
                {
                    "totalResults": null
                }
                """;

        HttpClient mockHttpClient = mock(HttpClient.class);
        HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.body()).thenReturn(mockResponse);
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockHttpResponse);

        Request mockRequest = mock(Request.class);
        when(mockRequest.build()).thenReturn("http://mock.api/newsfeed");

        Function<String, PagedResponse<Object>> mockParser = mock(Function.class);

        HTTPBaseClient<Object> client = new HTTPBaseClient
                .HTTPBaseClientBuilder<>(mockHttpClient, mockRequest, mockParser)
                .build();

        assertThrows(JSONParsingException.class, () -> client.loadPages(1),
                "Should throw JSONParsingException when 'totalResults' cannot be parsed");
    }
}

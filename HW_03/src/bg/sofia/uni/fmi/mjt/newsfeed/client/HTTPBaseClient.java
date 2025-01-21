package bg.sofia.uni.fmi.mjt.newsfeed.client;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ClientSendException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.JSONParsingException;
import bg.sofia.uni.fmi.mjt.newsfeed.request.NewsFeedRequest;
import bg.sofia.uni.fmi.mjt.newsfeed.request.Request;
import bg.sofia.uni.fmi.mjt.newsfeed.response.PagedResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class HTTPBaseClient<T> implements Client<T> {

    private final HttpClient httpClient;
    private final List<T> data=new ArrayList<>();
    private Request currentRequest;
    private final Function<String, PagedResponse> parser;
    private int pagesLimit;
    private boolean isFinalPage=false;

    protected HTTPBaseClient(HTTPBaseClientBuilder builder){
        this.httpClient=builder.httpClient;
        this.pagesLimit= builder.pagesLimit;
        this.parser=builder.parser;
        currentRequest=builder.request;
    }

    @Override
    public List<T> loadAll()throws ClientSendException, JSONParsingException{
        return loadPages(pagesLimit);
    }

    @Override
    public List<T> loadPages(int numPages) throws ClientSendException, JSONParsingException {
        List<T> result = new ArrayList<>();
        for (int i = 1; i <= numPages && !isFinalPage; i++) {
            result.addAll(execute(i));
            currentRequest.updatePage(i);
        }
        return result;
    }

    private List<T> parseResponse(String responseJSON) {
        PagedResponse response = parser.apply(responseJSON);
        data.addAll(response.getData());
        return response.getData();
    }

    private List<T> execute(int page) throws ClientSendException, JSONParsingException {
        String currentUri = currentRequest.build();
        URI uri = URI.create(currentUri);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(uri)
                .build();

        String responseJSON;

        try {
            responseJSON = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new ClientSendException("Cant send the request", e);
        }
        JsonElement errors = JsonParser.parseString(responseJSON).getAsJsonObject().get("errors");
        if (errors != null) {
            throw new JSONParsingException("Errors while parsing the JSON");
        }

        //TODO remove
        JsonElement totalResultsElement = JsonParser.parseString(responseJSON).getAsJsonObject().get("totalResults");
        if (totalResultsElement != null && totalResultsElement.isJsonPrimitive()) {
            int totalResults = totalResultsElement.getAsInt();
            if(totalResults < page*currentRequest.pageSize()){
                isFinalPage=true;
            }
        } else {
            System.out.println("Total Results field is not available or is malformed");
        }

        return parseResponse(responseJSON);
    }

    public HTTPBaseClientBuilder<T> builder(Request request, Function<String, PagedResponse<T>> parser){
        return new HTTPBaseClientBuilder<>(request,parser);
    }

    public HTTPBaseClientBuilder<T> builder(HttpClient client,Request request, Function<String, PagedResponse<T>> parser){
        return new HTTPBaseClientBuilder<>(client,request,parser);
    }

    public static class HTTPBaseClientBuilder<T>{
        private final Request request;
        private final Function<String, PagedResponse<T>>parser;
        private HttpClient httpClient;
        private int pagesLimit;

        private static final int DEFAULT_PAGES_LIMIT=5;

        public  HTTPBaseClientBuilder(HttpClient httpClient,Request request,Function<String,PagedResponse<T>>parser){
            this.httpClient=httpClient;
            this.request=request;
            this.parser=parser;
            this.pagesLimit=DEFAULT_PAGES_LIMIT;
        }

        public  HTTPBaseClientBuilder(Request request,Function<String,PagedResponse<T>>parser){
            this.request=request;
            this.parser=parser;
            this.pagesLimit=DEFAULT_PAGES_LIMIT;
            httpClient=HttpClient.newBuilder().build();
        }

        public HTTPBaseClientBuilder setClient(HttpClient client){
            this.httpClient=client;
            return this;
        }

        public HTTPBaseClientBuilder setPagesLimit(int pagesLimit){
            if(pagesLimit<1){
                throw new IllegalArgumentException("Pages Limit cant be smaller than 1");
            }
            this.pagesLimit=pagesLimit;
            return this;
        }

        public HTTPBaseClient<T> build(){
            return new HTTPBaseClient<>(this);
        }
    }
}

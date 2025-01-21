package bg.sofia.uni.fmi.mjt.newsfeed.client;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ClientSendException;
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

public class HTTPBaseClient<T> implements Client<T>{

    private final HttpClient httpClient;
    private final List<T> data;
    private final int defaultNumberOfPages;
    private Request currentRequest;
    private final Function<String, PagedResponse> parser;

    @Override
    public List<T> loadPages(int numPages) {
        List<T>result= new ArrayList<>();
        for(int i=0;i<numPages;i++){
            result.addAll()
        }

    }

    private List<T>execute()throws ClientSendException{
        String currentUri= currentRequest.build();
        if(currentUri==null){
            throw new IllegalArgumentException("request ended");
        }
        URI uri =URI.create(currentUri);
        HttpRequest request= HttpRequest
                .newBuilder()
                .uri(uri)
                .build();

        String responseJSON;

        try {
            responseJSON=httpClient.send(request,HttpResponse.BodyHandlers.ofString()).body()
        }catch (IOException |InterruptedException e){
            throw new ClientSendException("Cant send the request",e);
        }
        JsonElement errors = JsonParser.parseString(json).getAsJsonObject().get("errors");
    }
}

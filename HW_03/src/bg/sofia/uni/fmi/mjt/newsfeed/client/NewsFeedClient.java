package bg.sofia.uni.fmi.mjt.newsfeed.client;

import bg.sofia.uni.fmi.mjt.newsfeed.news.Article;
import bg.sofia.uni.fmi.mjt.newsfeed.request.Request;
import bg.sofia.uni.fmi.mjt.newsfeed.response.NewsFeedResponse;

import java.net.http.HttpClient;

public class NewsFeedClient extends HTTPBaseClient<Article> {

    private NewsFeedClient(NewsFeedClientBuilder builder) {
        super(builder);
    }

    public static NewsFeedClientBuilder builder(Request request) {
        return new NewsFeedClientBuilder(request);
    }

    public static NewsFeedClientBuilder builder(HttpClient client, Request request) {
        return new NewsFeedClientBuilder(client, request);
    }

    public static class NewsFeedClientBuilder extends HTTPBaseClient.HTTPBaseClientBuilder<Article> {

        public NewsFeedClientBuilder(Request request) {
            super(request, NewsFeedResponse::of);
        }

        public NewsFeedClientBuilder(HttpClient client, Request request) {
            super(client, request, NewsFeedResponse::of);
        }

        @Override
        public NewsFeedClient build() {
            return new NewsFeedClient(this);
        }
    }
}

package bg.sofia.uni.fmi.mjt.newsfeed.client;

import java.util.List;

public class NewsFeedClient<T> implements Client<T>{

    private final HTTP

    @Override
    public List<T> loadPages(int numPages) {
        return List.of();
    }
}

package bg.sofia.uni.fmi.mjt.newsfeed.client;

import java.util.List;

public interface Client<T> {

    List<T>loadPages(int numPages);
}

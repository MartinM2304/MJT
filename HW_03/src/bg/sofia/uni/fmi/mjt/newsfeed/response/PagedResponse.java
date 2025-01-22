package bg.sofia.uni.fmi.mjt.newsfeed.response;

import java.util.List;

public interface PagedResponse<T> extends Response {

    List<T> getData();
}

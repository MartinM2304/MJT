package bg.sofia.uni.fmi.mjt.newsfeed.response;

import bg.sofia.uni.fmi.mjt.newsfeed.news.Article;
import bg.sofia.uni.fmi.mjt.newsfeed.request.Request;

import java.util.List;

public interface PagedResponse<T> extends Response{

    List<T> getData();
    //Request nextPage();
    //boolean isLastPage();
}

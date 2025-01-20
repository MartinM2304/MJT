package bg.sofia.uni.fmi.mjt.newsfeed.response;

public class NewsFeedResponse implements PagedResponse {
    @Override
    public boolean isLastPage() {
        return false;
    }

    @Override
    public String getStatus() {
        return "";
    }
}

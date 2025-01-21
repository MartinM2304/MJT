package bg.sofia.uni.fmi.mjt.newsfeed.request;

public interface Request {
    String build();
    Request updatePage(int newPage);
    int pageSize();
}

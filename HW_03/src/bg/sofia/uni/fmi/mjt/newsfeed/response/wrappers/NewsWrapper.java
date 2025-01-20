package bg.sofia.uni.fmi.mjt.newsfeed.response.wrappers;

import bg.sofia.uni.fmi.mjt.newsfeed.news.Article;

import java.util.List;

public class NewsWrapper implements Wrappable<List<Article>>{

    private final String status;
    private final int totalResults;
    private final List<Article> articles;

    //TODO
    private void valdiate(String status,int totalResults, List<Article> articles){

    }

    public NewsWrapper(String status,int totalResults, List<Article> articles){
        valdiate(status, totalResults, articles);
        this.status=status;
        this.totalResults=totalResults;
        this.articles=articles;
    }
    @Override
    public List<Article> unwrap() {
        return articles;
    }
}

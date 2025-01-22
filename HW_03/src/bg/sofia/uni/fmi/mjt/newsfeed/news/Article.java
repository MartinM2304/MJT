package bg.sofia.uni.fmi.mjt.newsfeed.news;

import java.net.URL;
import java.util.Date;

public record Article(
        Source source,
        String author,
        String title,
        String description,
        URL url,
        URL urlToImage,
        Date publishedAt,
        String content
) { }

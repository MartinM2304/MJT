package bg.sofia.uni.fmi.mjt.newsfeed.news;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Date;

public class Article {

    private Source source;
    private String author;
    private String title;
    private String description;
    private URL url;
    private URL urlToImage;
    private Date publishedAt;
    private String content;

    private boolean isImageLoaded;

    private void validateArticle(Source source, String author, String title,
                                 String description, URL url, URL imageUrl, String publishedAt, String content){

    }

//    private void getImageFromUrl(URL imageUrl){
//        try {
//            image= ImageIO.read(imageUrl);
//        }catch (IOException e){
//            image=null;
//            isImageLoaded=false;
//        }
//    }

    private void getDateTime(String timestamp){
        publishedAt = Date.from(Instant.parse(timestamp));
    }

    public Article(Source source, String author, String title,
                   String description, URL url, URL imageUrl, String publishedAt, String content){

        validateArticle(source, author, title, description, url, imageUrl, publishedAt, content);
        isImageLoaded=true;
        this.urlToImage=imageUrl;
        getDateTime(publishedAt);
    }

    public Source getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public URL getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public boolean isImageLoaded() {
        return isImageLoaded;
    }

    @Override
    public String toString() {
        return "Article{" +
                "source=" + (source != null ? source.toString() : "null") +
                ", author='" + (author != null ? author : "N/A") + '\'' +
                ", title='" + (title != null ? title : "N/A") + '\'' +
                ", description='" + (description != null ? description : "N/A") + '\'' +
                ", url=" + (url != null ? url.toString() : "N/A") +
                ", image=" + (urlToImage != null ? "Image Loaded" : "Image Not Loaded") +
                ", publishedAt=" + (publishedAt != null ? publishedAt.toString() : "N/A") +
                ", content='" + (content != null ? content : "N/A") + '\'' +
                ", isImageLoaded=" + isImageLoaded +
                '}';
    }
}

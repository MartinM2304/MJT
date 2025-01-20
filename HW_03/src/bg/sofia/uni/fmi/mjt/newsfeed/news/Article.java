package bg.sofia.uni.fmi.mjt.newsfeed.news;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;

public class Article {

    private static final String IMAGE_NOT_LOADED="Image";
    //    private class Source{
//        String id;
//        String name;
//    }
    private Source source;
    private String author;
    private String title;
    private String description;
    private URL url;
    private Image image;
    private Instant publishedAt;
    private String content;

    private boolean isImageLoaded;

    private void validateArticle(Source source, String author, String title,
                                 String description, URL url, URL imageUrl, String publishedAt, String content){

    }

    private void getImageFromUrl(URL imageUrl){
        try {
            image= ImageIO.read(imageUrl);
        }catch (IOException e){
            image=null;
            isImageLoaded=false;
        }
    }

    private void getDateTime(String timestamp){
        publishedAt = Instant.parse(timestamp);
    }

    public Article(Source source, String author, String title,
                   String description, URL url, URL imageUrl, String publishedAt, String content){

        validateArticle(source, author, title, description, url, imageUrl, publishedAt, content);
        isImageLoaded=true;
        getImageFromUrl(imageUrl);
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

    public Image getImage() {
        return image;
    }

    public String getContent() {
        return content;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public boolean isImageLoaded() {
        return isImageLoaded;
    }
}

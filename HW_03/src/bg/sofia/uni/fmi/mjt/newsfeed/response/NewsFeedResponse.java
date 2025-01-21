package bg.sofia.uni.fmi.mjt.newsfeed.response;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsApiErrorException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ResponseIncorrect;
import bg.sofia.uni.fmi.mjt.newsfeed.news.Article;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

public class NewsFeedResponse implements PagedResponse {
    private String status;
    private List<Article> articles;
    //private final Request nextPage;

    public NewsFeedResponse(String json){
        JsonElement parser= JsonParser.parseString(json);
        if (!parser.isJsonObject()) {
            throw new ResponseIncorrect("Root element is not a JSON object");
        }

        var jsonObject= parser.getAsJsonObject();
        if(jsonObject.has("status")){
            status=jsonObject.get("status").getAsString();
        }else{
            throw new ResponseIncorrect("Status field is missing from response");
        }
        handleErrors(status);

        extractArticles(jsonObject);

    }



    private String getErrorMessage(String status){
        String errorCode = "Unknown code";
        String errorMessage = "Unknown error";

        JsonElement parser = JsonParser.parseString(status);
        if (parser.isJsonObject()) {
            var jsonObject = parser.getAsJsonObject();
            if (jsonObject.has("code")) {
                errorCode = jsonObject.get("code").getAsString();
            }
            if (jsonObject.has("message")) {
                errorMessage = jsonObject.get("message").getAsString();
            }
        }

        return "Error occurred. Code: " + errorCode + ", Message: " + errorMessage;
    }

    private void handleErrors(String status){
        if (!"ok".equalsIgnoreCase(status)) {
            throw new NewsApiErrorException(getErrorMessage(status));
        }
    }

    private void extractArticles(JsonObject jsonObject){
        if (jsonObject.has("articles")) {
            var articlesArray = jsonObject.getAsJsonArray("articles");
            articles = new Gson().fromJson(articlesArray, new com.google.gson.reflect.TypeToken<List<Article>>() {}.getType());
        } else {
            throw new ResponseIncorrect("Missing 'articles' field");
        }
    }


    @Override
    public String getStatus() {
        return status;
    }

    public List<Article> getData() {
        return articles;
    }

    public static NewsFeedResponse of (String json){
        return new NewsFeedResponse(json);
    }

}
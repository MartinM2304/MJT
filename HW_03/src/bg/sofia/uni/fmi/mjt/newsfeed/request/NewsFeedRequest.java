package bg.sofia.uni.fmi.mjt.newsfeed.request;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class NewsFeedRequest implements Request {

    private static final String BASE_URL = "https://newsapi.org/v2/top-headlines";
    private static final String API_KEY = "83badb6fcfc449e3a6765bd41e9b440b";

    private static final String QUERY_PARAM = "q";
    private static final String CATEGORY_PARAM = "category";
    private static final String COUNTRY_PARAM = "country";
    private static final String API_KEY_PARAM = "apiKey";

    private final Set<String> keyWords ;
    private final Set<String> categories ;
    private final Set<String> countries ;

    public NewsFeedRequest(NewsFeedBuilder builder){
        keyWords=builder.keyWords;
        categories=builder.categories;
        countries=builder.countries;
    }

    private String appendParameters(StringBuilder result,String parameter){
        result.append(parameter).append("&");
        return result.toString();
    }

    private String encodeParameter(String key, Set<String> values) {
        if (values.isEmpty()) {
            return "";
        }
        return key + "=" + String.join(",", values.stream()
                .map(this::encodeValue)
                .collect(Collectors.toList())) + "&";
    }

    private String encodeValue(String value) {
        return value.replace(" ", "%20").replace("\"", "%22").replace("+", "%2B");
    }

    private String getSearchParameters(){
        StringBuilder result = new StringBuilder();
        result.append(encodeParameter(QUERY_PARAM, keyWords));
        result.append(encodeParameter(CATEGORY_PARAM, categories));
        result.append(encodeParameter(COUNTRY_PARAM, countries));
        result.append(API_KEY_PARAM).append("=").append(API_KEY);
        return result.toString();
    }

    @Override
    public String build() {
        return BASE_URL+"?"+getSearchParameters();
    }


    public static NewsFeedBuilder getBuilder(){
        return new NewsFeedBuilder();
    }

    public static class NewsFeedBuilder {
        private final Set<String> keyWords = new HashSet<>();
        private final Set<String> categories = new HashSet<>();
        private final Set<String> countries = new HashSet<>();

        private NewsFeedBuilder() {}

        public NewsFeedBuilder setKeyWords(Collection<String> keyWords){
            if(keyWords==null){
                throw new IllegalArgumentException("keywords are null");
            }
            this.keyWords.addAll(keyWords);
            return this;
        }

        public NewsFeedBuilder setCategories(Collection<String> categories) {
            if (categories == null) {
                throw new IllegalArgumentException("Categories cannot be null");
            }
            this.categories.addAll(categories);
            return this;
        }

        public NewsFeedBuilder setCountries(Collection<String> countries) {
            if (countries == null) {
                throw new IllegalArgumentException("Countries cannot be null");
            }
            this.countries.addAll(countries);
            return this;
        }

        public NewsFeedRequest build(){
            return new NewsFeedRequest(this);
        }
    }

}

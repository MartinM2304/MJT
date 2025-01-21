package bg.sofia.uni.fmi.mjt.newsfeed.request;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.IllegalRequestParameter;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsApiFreeException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NewsFeedRequest implements Request {

    private static final String BASE_URL = "https://newsapi.org/v2/top-headlines";
    //private static final String API_KEY = "83badb6fcfc449e3a6765bd41e9b440b";

    private static final String QUERY_PARAM = "q";
    private static final String CATEGORY_PARAM = "category";
    private static final String COUNTRY_PARAM = "country";
    private static final String API_KEY_PARAM = "apiKey";
    private static final String PAGE_PARAM = "page";
    private static final String PAGE_SIZE_PARAM = "pageSize";
    private static final int PAGES_DEFAULT_SIZE = 20;
    private static final int PAGES_DEFAULT_NUMBER = 1;
    private static final int FREE_NUMBER_OF_PAGES=100;

    private final String apiKey;
    private final Set<String> keyWords;
    private final Set<String> categories;
    private final Set<String> countries;
    private int page;
    private int pageSize;

    public NewsFeedRequest(NewsFeedBuilder builder) {
        keyWords = builder.keyWords;
        if(keyWords==null|| keyWords.isEmpty()){
            throw new IllegalRequestParameter("Cant make Request withour keywords");
        }
        categories = builder.categories;
        countries = builder.countries;
        page=builder.page;
        pageSize= builder.pageSize;
        apiKey=builder.apiKey;
    }

    @Override
    public NewsFeedRequest updatePage(int newPage){
        if(newPage<0){
            throw new IllegalArgumentException("pages must be more than 0");
        }
        if(newPage*pageSize>FREE_NUMBER_OF_PAGES){
            pageSize=FREE_NUMBER_OF_PAGES-(page-1)*pageSize;//using constant -1 as it is for previous page and is always -1
            //throw new NewsApiFreeException("Requested result is more than the limit of the free api");
        }
        return this;
    }

    private String appendParameters(StringBuilder result, String parameter) {
        result.append(parameter).append("&");
        return result.toString();
    }

    private String encodeParameter(String key, Set<String> values) {
        if (values.isEmpty()) {
            return "";
        }
        return key + "=" + String.join("+", values.stream()
                //.map(this::encodeValue)
                .collect(Collectors.toList())) + "&";
    }

//    private String encodeValue(String value) {
//        return value.replace(" ", "%20").replace("\"", "%22").replace("+", "%2B");
//    }

    private String getSearchParameters() {
        StringBuilder result = new StringBuilder();
        result.append(encodeParameter(QUERY_PARAM, keyWords));
        result.append(encodeParameter(CATEGORY_PARAM, categories));
        result.append(encodeParameter(COUNTRY_PARAM, countries));
        if (page > 0) {
            result.append(PAGE_PARAM).append("=").append(page).append("&");
        }
        if (pageSize > 0) {
            result.append(PAGE_SIZE_PARAM).append("=").append(pageSize).append("&");
        }
        result.append(API_KEY_PARAM).append("=").append(apiKey);
        return result.toString();
    }

    @Override
    public String build() {
        return BASE_URL + "?" + getSearchParameters();
    }

    @Override
    public int pageSize(){
        return pageSize;
    }


    public static NewsFeedBuilder getBuilder(String apiKey) {
        return new NewsFeedBuilder(apiKey);
    }

    public static class NewsFeedBuilder {
        private final Set<String> keyWords = new HashSet<>();
        private final Set<String> categories = new HashSet<>();
        private final Set<String> countries = new HashSet<>();
        private int page = PAGES_DEFAULT_NUMBER;
        private int pageSize = PAGES_DEFAULT_SIZE;
        private final String apiKey;

        public NewsFeedBuilder(String apiKey) {
            if(apiKey==null||apiKey.isEmpty()||apiKey.isBlank()){
                throw new IllegalArgumentException("Api key cant be empty");
            }
            this.apiKey=apiKey;
        }


        public NewsFeedBuilder addKeyWords(Collection<String> keyWords) {
            if (keyWords == null) {
                throw new IllegalArgumentException("keywords are null");
            }
            this.keyWords.addAll(keyWords);
            return this;
        }

        public NewsFeedBuilder addCategories(Collection<String> categories) {
            if (categories == null) {
                throw new IllegalArgumentException("Categories cannot be null");
            }
            this.categories.addAll(categories);
            return this;
        }

        public NewsFeedBuilder addCountries(Collection<String> countries) {
            if (countries == null) {
                throw new IllegalArgumentException("Countries cannot be null");
            }
            this.countries.addAll(countries);
            return this;
        }

        public NewsFeedBuilder setPage(int page) {
            if (page < 1) {
                throw new IllegalArgumentException("Page number must be greater than 0");
            }
            this.page = page;
            return this;
        }

        public NewsFeedBuilder setPageSize(int pageSize) {
            if (pageSize < 1 || pageSize > 100) {
                throw new IllegalArgumentException("Page size must be between 1 and 100");
            }
            this.pageSize = pageSize;
            return this;
        }

        public NewsFeedRequest build() {
            return new NewsFeedRequest(this);
        }
    }

}

package bg.sofia.uni.fmi.mjt.newsfeed.request;

public class NextPageRequest implements Request{
    private String nextPageURI;

    private void validate(String nextPageURI){
        if(nextPageURI==null|| nextPageURI.isBlank()||nextPageURI.isEmpty()){
            throw new IllegalArgumentException("nextPageURI cant be blanks");
        }
    }
    public NextPageRequest(String nextPageURI) {
        validate(nextPageURI);
        this.nextPageURI = nextPageURI;
    }

    @Override
    public String build() {
        return nextPageURI;
    }
}

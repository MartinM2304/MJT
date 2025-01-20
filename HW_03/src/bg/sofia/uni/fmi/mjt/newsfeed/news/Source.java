package bg.sofia.uni.fmi.mjt.newsfeed.news;

public record Source(String id, String name) {

    public Source{
        if(id==null||name==null){
            throw new IllegalArgumentException("id or name cant be null");
        }
    }
}

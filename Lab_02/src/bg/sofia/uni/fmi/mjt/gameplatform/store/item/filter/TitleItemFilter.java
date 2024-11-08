package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class TitleItemFilter implements  ItemFilter{

    private  String title;
    private boolean caseSensitive;

    public TitleItemFilter(String title, boolean caseSensitive){
        if(title!= null){
            this.title=title;
        }

        this.caseSensitive=caseSensitive;
    }

    public  boolean matches(StoreItem item){

        if(!caseSensitive){
            return title.equalsIgnoreCase(item.getTitle());
        }
        if(item.getTitle().contains(this.title)){
            return  true;
        }

        return false;
    }
}

package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.Rater;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class RatingItemFilter implements ItemFilter {

    private double rating;

    public RatingItemFilter(double rating) {
        //this.rating= new Rater();
        //this.rating.rate(rating);
        if(rating>=1&& rating<=5){
            this.rating=rating;
        }
    }

    @Override
    public boolean matches(StoreItem item) {

        //int result = rating.compareTo(item.getRating());
        if(rating>item.getRating()){
            return false;
        }

        return true;
    //
    //        if (result == -1) {
    //            return false;
    //        }
    //        return true;
    }
}

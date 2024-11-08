package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReleaseDateItemFilter implements ItemFilter {

    private LocalDateTime lowerBound;
    private LocalDateTime upperBound;

    public ReleaseDateItemFilter(LocalDateTime lowerBound, LocalDateTime upperBound) {
        //TODO implement valdiation for input

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }


    @Override
    public boolean matches(StoreItem item) {

        LocalDateTime date=item.getReleaseDate();

        if(lowerBound.compareTo(date)<1 && upperBound.compareTo(date)>=0){
            return true;
        }
        return false;
    }
}

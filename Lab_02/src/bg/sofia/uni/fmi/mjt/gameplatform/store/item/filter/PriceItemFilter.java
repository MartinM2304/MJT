package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;

public class PriceItemFilter implements ItemFilter{

    private BigDecimal lowerBound;
    private BigDecimal upperBound;

    public PriceItemFilter(BigDecimal lowerBound, BigDecimal upperBound){
        //TODO VALIDATE INPUT
        this.lowerBound=lowerBound;
        this.upperBound=upperBound;
    }

    @Override
    public  boolean matches(StoreItem item){

        BigDecimal price= item.getPrice();
        if(price.compareTo(lowerBound)!=-1&& price.compareTo(upperBound)!=1){
            return true;
        }

        return false;
    }
}

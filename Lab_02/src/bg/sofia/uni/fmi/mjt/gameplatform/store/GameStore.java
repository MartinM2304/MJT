package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

import java.io.Console;
import java.math.BigDecimal;

public class GameStore implements  StoreAPI{

    private StoreItem[] availableItems;

    private  boolean van40;
    private  boolean stoyo;


    public GameStore(StoreItem[]availableItems){
        this.availableItems=availableItems;
    }

    public static boolean isItemInFilter(StoreItem item, ItemFilter[] filters){

        for(ItemFilter filter: filters ){
            boolean result= filter.matches(item);

            if(result==false){
                return false;
            }
        }

        return true;

    }

    @Override
    public StoreItem[] findItemByFilters(ItemFilter[] itemFilters) {

        StoreItem[]tmpResult = new StoreItem[availableItems.length];//using it with maximum capcity

        int size=0;

        for(StoreItem item:availableItems){
            boolean isFiltered= isItemInFilter(item,itemFilters);
            if(isFiltered){
                tmpResult[size++]=item;
            }
        }

        StoreItem[] result= new StoreItem[size];
        for(int i=0;i<size;i++){
            result[i]=tmpResult[i];
        }

        return result;
    }

    @Override
    public void applyDiscount(String promoCode) {
//        if(activePromo!= null&&this.activePromo.equals(promoCode)){
//            return;
//        }
//        activePromo=promoCode;

        //System.out.println("applyDiscont");
        double discount=1;

        if(promoCode.equals("VAN40")){

            if(van40){
                return;
            }
            van40=true;
            discount=0.6;// 100-40 =60%
        }else if(promoCode.equals("100YO")){
            if(stoyo){
                return;
            }
            stoyo=true;
            discount=0;
        }else{
            return;
            //System.out.println("Not valid code");
            //we still havent learned exceptions so i dont want to use them
        }

        BigDecimal disc=BigDecimal.valueOf(discount);
        if(discount==0){
            disc=BigDecimal.ZERO;
        }

        for(StoreItem item:availableItems){
            BigDecimal currentPrice=item.getPrice();
            item.setPrice(currentPrice.multiply(disc));

            //System.out.println(item.getPrice());
        }

    }

    @Override
    public boolean rateItem(StoreItem item, int rating) {
        if(rating<1 || rating>5){
            return false;
        }
        item.rate(rating);

        return true;
    }
}

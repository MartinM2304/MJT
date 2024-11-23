package bg.sofia.uni.fmi.mjt.gameplatform.store.item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.Rater;

/**
 * making the class abstract even though there are not abstract function,
 * because it all the functions in the StoreItem class are with the same behavior
 * for Game, DLS, GameBundle so StoreItemExtended can override them all
 * makin the class itslef abstract because there cant be existing
 * StoreItemExtended instance, in this case this class is only for code
 * reusabilty
 * P.S. asked one assistant and he said it is alright in this case
 */
public abstract class StoreItemExtended implements StoreItem{
    private String title;

    /**
    * Rate is a helper class to implement the rating logic for better abstraction
    *
    **/
    private Rater rating;

    private BigDecimal price;

    private LocalDateTime releaseDate;

    public StoreItemExtended(String title, BigDecimal price, LocalDateTime releaseData){
        this.rating=new Rater();
        this.setTitle(title);
        this.setPrice(price);
        this.setReleaseDate(releaseData);


    }


    @Override
    public String getTitle(){
        return  title;
    }

    @Override
    public  BigDecimal getPrice(){
        return price;
    }

    @Override
    public double getRating(){
        return rating.getRating();
    }

    @Override
    public  LocalDateTime getReleaseDate(){
        return  releaseDate;
    }

    @Override
    public  void setTitle(String title){
        if(title != null){
            this.title=title;
        }
    }

    @Override
    public void setPrice(BigDecimal price){


        if(price.signum()>=0){
            //System.out.println("inside if");
            this.price=price.setScale(2, RoundingMode.HALF_UP);
        }
        //System.out.println(this.price);
    }

    @Override
    public  void setReleaseDate(LocalDateTime releaseDate){
        this.releaseDate=releaseDate;
    }

    @Override
    public  void rate(double rating){
        this.rating.rate(rating);// the validation of the rating is in the Rater class itself
    }
}

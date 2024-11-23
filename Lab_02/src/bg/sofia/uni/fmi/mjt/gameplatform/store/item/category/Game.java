package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItemExtended;

public class Game extends StoreItemExtended {

    String genre;

    public Game(String title, BigDecimal price, LocalDateTime releaseDate, String genre){
        super(title,price,releaseDate);
        this.setGenre(genre);
    }

    void  setGenre(String genre){
        if(genre!=null) {
            this.genre = genre;
        }
    }

    public String getGenre(){
        return this.genre;
    }

}

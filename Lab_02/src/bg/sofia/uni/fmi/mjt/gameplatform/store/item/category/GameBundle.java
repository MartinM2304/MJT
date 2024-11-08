package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItemExtended;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GameBundle extends StoreItemExtended {

    Game[] games;

    public GameBundle(String title, BigDecimal price, LocalDateTime releaseDate, Game[] games){
        super(title,price,releaseDate);
        this.games=games;// in this case we want shallow copy so if somebody changes one game it will change in the bundle too.
    }
}

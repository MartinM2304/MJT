package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItemExtended;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DLC extends StoreItemExtended {

    private Game game;

    public DLC(String title, BigDecimal price, LocalDateTime releaseDate, Game game){
        super(title,price,releaseDate);
        this.game=game;
    }
}

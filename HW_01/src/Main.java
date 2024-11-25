import bg.sofia.uni.fmi.mjt.glovo.Glovo;
import bg.sofia.uni.fmi.mjt.glovo.GlovoApi;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or

//TODO add validation for matrix that isnt rectangle
public class Main {
    public static void main(String[] args) {

        char[][] layout = {
                {'#', '#', '#', '.', '#'},
                {'#', '.', 'B', 'R', 'A'},
                {'.', '.', '#', 'C', '#'},
                {'#', '.', '.', '.', '.'},
                {'#', 'A', '#', '#', '#'}
        };

        //System.out.println(layout[3][1]);

        GlovoApi glovo = new Glovo(layout);
        MapEntity client = new MapEntity(new Location(2, 3), MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(new Location(1, 3), MapEntityType.RESTAURANT);
        Delivery delivery = null;
        try {
            delivery = glovo.getCheapestDeliveryWithinTimeLimit(client, restaurant, "pizza", 9);
        } catch (NoAvailableDeliveryGuyException e) {
            System.out.println("Exception caught in Main");
        }
        System.out.println(delivery);
    }
}
package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.PathFinder;
import bg.sofia.uni.fmi.mjt.glovo.dataStructures.Pair;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;

import java.util.HashMap;
import java.util.Map;

public class ControlCenter implements ControlCenterApi {

    private char[][] mapLayout;
    /*
     * Used for optimization so if once all the paths from the specific restaurant have been found
     * To not run the algorithm to find paths( it works because the starting positions of neither is changing)
     * PathFinder contains all the paths from the specified restaurant to the rest MapEntities
     * and the paths from the restaurant to the closest car/bike deliveryGuys
     */
    private Map<MapEntity, PathFinder> restaurantsPaths;

    public ControlCenter(char[][] mapLayout) {
        if (mapLayout == null) {
            throw new IllegalArgumentException("mapLayout cannot be null");
        }
        this.mapLayout = mapLayout;
        restaurantsPaths = new HashMap<>();
    }

    private DeliveryInfo getFastestDeliveryGuy(MapEntity client, MapEntity restaurant, double maxPrice) {
        Pair<MapEntity, Integer> deliveryGuyAndPath = restaurantsPaths.get(restaurant).getDeliveryGuyBasedOnCriteria(ShippingMethod.FASTEST);
        double price = deliveryGuyAndPath.second * deliveryGuyAndPath.first.type().getDeliveryTypeIfCarOrBike().getPricePerKilometer();
        int time = deliveryGuyAndPath.second * deliveryGuyAndPath.first.type().getDeliveryTypeIfCarOrBike().getTimePerKilometer();

        DeliveryInfo deliveryInfo = null;
        if (maxPrice == -1 || price <= maxPrice) {
            return new DeliveryInfo(deliveryGuyAndPath.first.location(), price, time, deliveryGuyAndPath.first.type().getDeliveryTypeIfCarOrBike());
        }

        deliveryInfo = getCheapestDelivery(client, restaurant, -1);
        if (deliveryInfo.price() > maxPrice) {
            return null;
        }
        return deliveryInfo;

    }

    private DeliveryInfo getCheapestDelivery(MapEntity client, MapEntity restaurant, int maxTime) {
        Pair<MapEntity, Integer> deliveryGuyAndPath = restaurantsPaths.get(restaurant).getDeliveryGuyBasedOnCriteria(ShippingMethod.CHEAPEST);
        double price = deliveryGuyAndPath.second * deliveryGuyAndPath.first.type().getDeliveryTypeIfCarOrBike().getPricePerKilometer();
        int time = deliveryGuyAndPath.second * deliveryGuyAndPath.first.type().getDeliveryTypeIfCarOrBike().getTimePerKilometer();

        DeliveryInfo deliveryInfo = null;
        if (maxTime == -1 || time <= maxTime) {
            return new DeliveryInfo(deliveryGuyAndPath.first.location(), price, time, deliveryGuyAndPath.first.type().getDeliveryTypeIfCarOrBike());
        }

        deliveryInfo = getFastestDeliveryGuy(client, restaurant, -1);
        if (deliveryInfo.estimatedTime() > maxTime) {
            return null;
        }
        return deliveryInfo;
    }

    private void initRestaurantPaths(MapEntity client, MapEntity restaurant) {
        restaurantsPaths.put(restaurant, new PathFinder(client, restaurant, mapLayout));
    }


    /**
     * Finds the optimal delivery person for a given delivery task. The method
     * selects the best delivery option based on the provided cost and time constraints.
     * If no valid delivery path exists, it returns null.
     *
     * @param restaurantLocation The location of the restaurant to start the delivery from.
     * @param clientLocation     The location of the client receiving the delivery.
     * @param maxPrice           The maximum price allowed for the delivery. Use -1 for no cost constraint.
     * @param maxTime            The maximum time allowed for the delivery. Use -1 for no time constraint.
     * @param shippingMethod     The method for shipping the delivery.
     * @return A DeliveryInfo object containing the optimal delivery guy, the total cost,
     * the total time, and the delivery type. Returns null if no valid path is found.
     */
    public DeliveryInfo findOptimalDeliveryGuy(Location restaurantLocation, Location clientLocation,
                                               double maxPrice, int maxTime, ShippingMethod shippingMethod) {
        MapEntity client = new MapEntity(clientLocation, MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(restaurantLocation, MapEntityType.RESTAURANT);

        if (!restaurantsPaths.containsKey(restaurantLocation)) {
            initRestaurantPaths(client, restaurant);
        } else {
            restaurantsPaths.get(restaurantLocation).updateClient(client);
        }

        DeliveryInfo deliveryInfo = null;

        if (shippingMethod == ShippingMethod.FASTEST) {
            deliveryInfo = getFastestDeliveryGuy(client, restaurant, maxPrice);
        } else if (shippingMethod == ShippingMethod.CHEAPEST) {
            deliveryInfo = getCheapestDelivery(client, restaurant, maxTime);
        } else {
            throw new UnsupportedOperationException("there is not other shipping method than Fastest/Cheapest, if added please expand");
        }

        return deliveryInfo;
    }

    /**
     * Returns the map
     *
     * @return A MapEntity[][] containing the map
     */
    public MapEntity[][] getLayout() {
        //TODO
        int rows = mapLayout.length;
        int columns = mapLayout[0].length;
        MapEntity[][] layout = new MapEntity[rows][];
        for (int i = 0; i < rows; i++) {
            layout[i] = new MapEntity[columns];
            for (int j = 0; j < columns; j++) {
                layout[i][j] = PathFinder.getEntityFromLocation(new Location(i, j), mapLayout);
            }
        }
        return layout;
    }
}

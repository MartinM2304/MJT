package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.PathFinder;
import bg.sofia.uni.fmi.mjt.glovo.dataStructures.Pair;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;

import java.util.Map;

public class Glovo implements GlovoApi {

    private char[][] mapLayout;
    /*
     * Used for optimization so if once all the paths from the specific restaurant have been found
     * To not run the algorithm to find paths( it works because the starting positions of neither is changing)
     * PathFinder contains all the paths from the specified restaurant to the rest restaurants
     * and the paths from the restaurant to the closest car/bike deliveryGuys
     */
    private Map<MapEntity, PathFinder> restaurantsPaths;

    public Glovo(char[][] mapLayout) {
        if (mapLayout == null) {
            throw new IllegalArgumentException("mapLayout cannot be null");
        }
        this.mapLayout = mapLayout;
    }

    private void validate(MapEntity client, MapEntity restaurant, String foodItem) {
        if (client == null) {
            throw new IllegalArgumentException("client is null");
        }
        if (restaurant == null) {
            throw new IllegalArgumentException("restaurant is null");
        }
        if (foodItem == null || foodItem.isBlank()) {
            throw new IllegalArgumentException("foodItem is null or blank");
        }
    }

    private void initRestaurantPaths(MapEntity client, MapEntity restaurant, char[][] map) {
        restaurantsPaths.put(restaurant, new PathFinder(client, restaurant, map));
    }

    /**
     * Returns the cheapest delivery option for a specified food item from a restaurant to a client location.
     *
     * @param client     The delivery destination, represented by a MapEntity.
     * @param restaurant The location of the restaurant from which the food item is sourced,
     *                   represented by a MapEntity.
     * @param foodItem   The name of the food item to be delivered.
     * @return A Delivery object representing the cheapest available delivery option within the
     * specified constraints.
     * @throws InvalidOrderException           If there is no client or restaurant at the specified location on
     *                                         the map, or if the location is outside the map's defined boundaries.
     * @throws NoAvailableDeliveryGuyException If no delivery guys are available to complete the delivery.
     */
    public Delivery getCheapestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
            throws NoAvailableDeliveryGuyException {
        validate(client, restaurant, foodItem);

        if (!restaurantsPaths.containsKey(restaurant)) {
            initRestaurantPaths(client, restaurant, mapLayout);
        } else {
            restaurantsPaths.get(restaurant).updateClient(client);
        }
        Pair<MapEntity, Integer> deliveryGuyAndPricePair = restaurantsPaths.get(restaurant).getDeliveryGuyBasedOnCriteria(ShippingMethod.CHEAPEST);
        //Bike will always be the cheapest way
        double price = deliveryGuyAndPricePair.second * DeliveryType.BIKE.getPricePerKilometer();
        int time = deliveryGuyAndPricePair.second * DeliveryType.BIKE.getTimePerKilometer();
        return new Delivery(client.location(), restaurant.location(), deliveryGuyAndPricePair.first.location(), foodItem, price, time);
    }

    /**
     * Returns the fastest delivery option for a specified food item from a restaurant to a
     * client location.
     *
     * @param client     The delivery destination, represented by a MapEntity.
     * @param restaurant The location of the restaurant from which the food item is sourced,
     *                   represented by a MapEntity.
     * @param foodItem   The name of the food item to be delivered.
     * @return A Delivery object representing the fastest available delivery option within the specified
     * constraints.
     * @throws InvalidOrderException           If there is no client or restaurant at the specified location on
     *                                         the map, or if the location is outside the map's defined boundaries.
     * @throws NoAvailableDeliveryGuyException If no delivery guys are available to complete the delivery.
     */
    public Delivery getFastestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
            throws NoAvailableDeliveryGuyException {
        validate(client, restaurant, foodItem);
        if (!restaurantsPaths.containsKey(restaurant)) {
            initRestaurantPaths(client, restaurant, mapLayout);
        } else {
            restaurantsPaths.get(restaurant).updateClient(client);
        }

        Pair<MapEntity, Integer> deliveryGuyAndPricePair = restaurantsPaths.get(restaurant).getDeliveryGuyBasedOnCriteria(ShippingMethod.FASTEST);
        //Car will always be the cheapest way
        double price = deliveryGuyAndPricePair.second * DeliveryType.CAR.getPricePerKilometer();
        int time = deliveryGuyAndPricePair.second * DeliveryType.CAR.getTimePerKilometer();
        return new Delivery(client.location(), restaurant.location(), deliveryGuyAndPricePair.first.location(), foodItem, price, time);
    }

    /**
     * Returns the fastest delivery option under a specified price for a given food item from a restaurant
     * to a client location.
     *
     * @param client     The delivery destination, represented by a MapEntity.
     * @param restaurant The location of the restaurant from which the food item is sourced,
     *                   represented by a MapEntity.
     * @param foodItem   The name of the food item to be delivered.
     * @param maxPrice   The maximum price the client is willing to pay for the delivery.
     * @return A Delivery object representing the fastest available delivery option under the specified
     * price limit.
     * @throws InvalidOrderException           If there is no client or restaurant at the specified location
     *                                         on the map,or if the location is outside the map's defined boundaries.
     * @throws NoAvailableDeliveryGuyException If no delivery guys are available to complete the delivery.
     */
    public Delivery getFastestDeliveryUnderPrice(MapEntity client, MapEntity restaurant, String foodItem, double maxPrice)
            throws NoAvailableDeliveryGuyException {
        validate(client, restaurant, foodItem);

        if (!restaurantsPaths.containsKey(restaurant)) {
            initRestaurantPaths(client, restaurant, mapLayout);
        } else {
            restaurantsPaths.get(restaurant).updateClient(client);
        }

        Pair<MapEntity, Integer> deliveryGuyAndPricePair = restaurantsPaths.get(restaurant).getDeliveryGuyBasedOnCriteria(ShippingMethod.FASTEST);
        //Car will always be the cheapest way
        double price = deliveryGuyAndPricePair.second * DeliveryType.CAR.getPricePerKilometer();
        int time = deliveryGuyAndPricePair.second * DeliveryType.CAR.getTimePerKilometer();

        Delivery result = null;
        if (price > maxPrice) {
            result = getCheapestDelivery(client, restaurant, foodItem);
            if (result.getPrice() > maxPrice) {
                throw new NoAvailableDeliveryGuyException("There is no delivery guy who can fulfil the delivery for your price");
            }
        } else {
            result = new Delivery(client.location(), restaurant.location(), deliveryGuyAndPricePair.first.location(), foodItem, price, time);
        }

        return result;
    }

    /**
     * Returns the cheapest delivery option within a specified time limit for a given food item from a restaurant
     * to a client location.
     *
     * @param client     The delivery destination, represented by a MapEntity.
     * @param restaurant The location of the restaurant from which the food item is sourced,
     *                   represented by a MapEntity.
     * @param foodItem   The name of the food item to be delivered.
     * @param maxTime    The maximum allowable delivery time in minutes.
     * @return A Delivery object representing the cheapest available delivery option within the specified
     * time limit.
     * @throws InvalidOrderException           If there is no client or restaurant at the specified location
     *                                         on the map, or if the location is outside the map's defined boundaries.
     * @throws NoAvailableDeliveryGuyException If no delivery guys are available to complete the delivery.
     */
    public Delivery getCheapestDeliveryWithinTimeLimit(MapEntity client, MapEntity restaurant, String foodItem, int maxTime)
            throws NoAvailableDeliveryGuyException {
        validate(client, restaurant, foodItem);

        if (!restaurantsPaths.containsKey(restaurant)) {
            initRestaurantPaths(client, restaurant, mapLayout);
        } else {
            restaurantsPaths.get(restaurant).updateClient(client);
        }

        Pair<MapEntity, Integer> deliveryGuyAndPricePair = restaurantsPaths.get(restaurant).getDeliveryGuyBasedOnCriteria(ShippingMethod.CHEAPEST);
        //Car will always be the cheapest way
        double price = deliveryGuyAndPricePair.second * DeliveryType.BIKE.getPricePerKilometer();
        int time = deliveryGuyAndPricePair.second * DeliveryType.BIKE.getTimePerKilometer();

        Delivery result = null;
        if (time > maxTime) {
            result = getFastestDelivery(client, restaurant, foodItem);
            if (result.getEstimatedTime() > maxTime) {
                throw new NoAvailableDeliveryGuyException("There is no delivery guy who can fulfil the delivery for your time");
            }
        } else {
            result = new Delivery(client.location(), restaurant.location(), deliveryGuyAndPricePair.first.location(), foodItem, price, time);
        }

        return result;
    }
}

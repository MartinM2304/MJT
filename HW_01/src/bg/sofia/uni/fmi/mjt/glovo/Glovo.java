package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenterApi;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidMapException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;

public class Glovo implements GlovoApi {

    private final char[][] mapLayout;
    ControlCenterApi controlCenterApi;
    public static boolean debug = true;

    public Glovo(char[][] mapLayout) {
        validateMap(mapLayout);
        this.mapLayout = mapLayout;
        controlCenterApi = new ControlCenter(mapLayout);
    }

    public static void validateMap(char[][] mapLayout) {
        if (mapLayout == null) {
            throw new InvalidMapException("mapLayout cannot be null");
        }
        int rows = mapLayout.length;
        int columns = 0;
        for (int i = 0; i < rows; i++) {
            if (mapLayout[i] == null) {
                throw new InvalidMapException("the " + i + "'th row is null");
            }
            if (i == 0) {
                columns = mapLayout[0].length;
            }
            if (columns != mapLayout[i].length) {
                throw new InvalidMapException("the " + i + "'th rows length is different than the rest");
            }
            for (int j = 0; j < columns; j++) {
                MapEntityType type = MapEntityType.fromChar(mapLayout[i][j]);
            }
        }
    }

    private void validateClient(MapEntity client) {
        if (client == null) {
            throw new InvalidOrderException("client is null");
        }
        if (client.type() != MapEntityType.CLIENT || MapEntity.getEntityFromLocation(
                client.location(), mapLayout).type() != MapEntityType.CLIENT) {
            throw new InvalidOrderException(
                    "clients type is not client or the coordinates doesnt correspond to a client");
        }
        Location.validateLocationBasedOnMap(client.location(), mapLayout.length, mapLayout[0].length);
    }

    private void validateRestaurant(MapEntity restaurant) {
        if (restaurant == null) {
            throw new InvalidOrderException("restaurant is null");
        }
        if (restaurant.type() != MapEntityType.RESTAURANT
                || MapEntity.getEntityFromLocation(restaurant.location(), mapLayout)
                .type() != MapEntityType.RESTAURANT) {
            throw new InvalidOrderException(
                    "restaurant type is not client or the coordinates doesnt correspond to a restaurant");
        }
        Location.validateLocationBasedOnMap(restaurant.location(), mapLayout.length, mapLayout[0].length);
    }

    private void validateDelivery(MapEntity client, MapEntity restaurant, String foodItem) {
        validateClient(client);
        validateRestaurant(restaurant);

        if (foodItem == null || foodItem.isBlank()) {
            throw new IllegalArgumentException("foodItem is null or blank");
        }
    }

    private Delivery returnDelivery(DeliveryInfo deliveryInfo, MapEntity client, MapEntity restaurant, String foodItem)
            throws NoAvailableDeliveryGuyException {
        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException("There is no free deliveryGuy now");
        }

        Delivery result = new Delivery(deliveryInfo, client.location(), restaurant.location(), foodItem);
        return result;
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
        validateDelivery(client, restaurant, foodItem);

        DeliveryInfo deliveryInfo = null;
        deliveryInfo = controlCenterApi.findOptimalDeliveryGuy(
                restaurant.location(), client.location(), -1, -1, ShippingMethod.CHEAPEST);

        controlCenterApi.getLayout();
        return returnDelivery(deliveryInfo, client, restaurant, foodItem);
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
        validateDelivery(client, restaurant, foodItem);

        DeliveryInfo deliveryInfo = null;
        deliveryInfo = controlCenterApi.findOptimalDeliveryGuy(
                restaurant.location(), client.location(), -1, -1, ShippingMethod.FASTEST);

        return returnDelivery(deliveryInfo, client, restaurant, foodItem);
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
    public Delivery getFastestDeliveryUnderPrice(MapEntity client, MapEntity restaurant
            , String foodItem, double maxPrice)
            throws NoAvailableDeliveryGuyException {
        validateDelivery(client, restaurant, foodItem);

        DeliveryInfo deliveryInfo = null;
        deliveryInfo = controlCenterApi.findOptimalDeliveryGuy(
                restaurant.location(), client.location(), maxPrice, -1, ShippingMethod.FASTEST);
        return returnDelivery(deliveryInfo, client, restaurant, foodItem);
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
    public Delivery getCheapestDeliveryWithinTimeLimit(MapEntity client, MapEntity restaurant
            , String foodItem, int maxTime) throws NoAvailableDeliveryGuyException {
        validateDelivery(client, restaurant, foodItem);

        DeliveryInfo deliveryInfo = null;
        deliveryInfo = controlCenterApi.findOptimalDeliveryGuy(
                restaurant.location(), client.location(), -1, maxTime, ShippingMethod.CHEAPEST);
        return returnDelivery(deliveryInfo, client, restaurant, foodItem);
    }
}

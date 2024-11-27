package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import bg.sofia.uni.fmi.mjt.glovo.Glovo;
import bg.sofia.uni.fmi.mjt.glovo.datastructures.Pair;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.ClientNotAccessibleException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/*
 * PathFinder contains the restaurant it was initialized with and the closest paths from this restaurant to the
 * rest of the map, including all clients and the deliveryGuys nearest to them (closest with car and closest with bike)
 * as the rest are not needed as if one delivery guys is farther he will for sure take more time and money than the
 * delivery guy who is closer
 */

public class PathFinder {

    private final MapEntity restaurant;
    private MapEntity client;
    private final char[][] map;
    private Pair<MapEntity, Integer> closestBike;
    private Pair<MapEntity, Integer> closestCar;
    private int restaurantToClientKilometers;
    Map<Location, Integer> distances = new HashMap<>();

    public PathFinder(MapEntity client, MapEntity restaurant, char[][] map) {
        //TODO add validation
        this.client = client;
        this.restaurant = restaurant;
        this.map = map;
        closestBike = new Pair<>(null, Integer.MAX_VALUE);
        closestCar = new Pair<>(null, Integer.MAX_VALUE);
        findAllPathsInMap();
    }

    //TODO ask if it is good like that
    public static MapEntity getEntityFromLocation(Location location, char[][] map) {
        int x = location.x();
        int y = location.y();
        MapEntityType type = MapEntityType.fromChar(map[x][y]);

        if (Glovo.debug && type == MapEntityType.CLIENT) {
            System.out.printf("x= %d : y= %s\n", x, y);
        }
        return new MapEntity(location, type);
    }

    private MapEntity getEntityFromLocation(Location location) {
        return getEntityFromLocation(location, map);
    }

    /*
     * Iterates through the whole map and finds the shortest path between the client and the restaurant
     * and finds the closes delivery guy with car/bike to the restaurant. It is done in the same function
     * for better efficiency as we have to iterate through the map only once for both the client and the delivery guys.
     * The logic for finding the delivery guys is done in the function updateClosestBikeAndCar for better code styling
     * and better understanding. The logic for finding the shortest path to the customer is in the end of the function
     * as it is only one line there is no point in putting it in another function.
     */
    private void findAllPathsInMap() {
        Queue<MapEntity> queue = new LinkedList<>();
        int rows = map.length;
        int columns = map[0].length;
        distances.put(restaurant.location(), 0);
        queue.add(restaurant);

        while (!queue.isEmpty()) {
            MapEntity current = queue.peek();
            queue.poll();
            for (Location neighborLocation : current.getNeighbors(rows, columns)) {
                MapEntity neighbor = getEntityFromLocation(neighborLocation);
                if (neighbor.type() == MapEntityType.WALL) {
                    continue;
                }
                int currentDistance = distances.get(current.location()) + 1;

                // we already visited this node
                if (distances.containsKey(neighborLocation)) {
                    int oldDistance = distances.get(neighborLocation);
                    if (currentDistance < oldDistance) {
                        queue.add(neighbor);
                        distances.put(neighbor.location(), currentDistance);
                        updateClosestBikeAndCar(neighbor, currentDistance);
                    }
                } else {
                    queue.add(neighbor);
                    distances.put(neighbor.location(), currentDistance);
                    updateClosestBikeAndCar(neighbor, currentDistance);
                }
            }
        }
        if (!distances.containsKey(client.location())) {
            throw new ClientNotAccessibleException("client is not accesiblee");
        }
        restaurantToClientKilometers = distances.get(client.location());
    }

    /*
     *   TODO add description
     */
    private void updateClosestBikeAndCar(MapEntity entity, int currentDistance) {
        if (entity.type() == MapEntityType.DELIVERY_GUY_BIKE) {
            if (currentDistance < closestBike.second) {
                closestBike.second = currentDistance;
                closestBike.first = entity;
            }
        } else if (entity.type() == MapEntityType.DELIVERY_GUY_CAR) {
            if (currentDistance < closestCar.second) {
                closestCar.second = currentDistance;
                closestCar.first = entity;
            }
        }
    }

    public MapEntity updateClient(MapEntity client) {
        this.client = client;
        restaurantToClientKilometers = distances.get(client.location());
        return this.client;
    }

    private int getDistanceWithFastShippingMethod(Pair<MapEntity, Integer> result) {
        int timeToCar = closestCar.second * DeliveryType.CAR.getTimePerKilometer();
        timeToCar += restaurantToClientKilometers * DeliveryType.CAR.getTimePerKilometer();

        int timeToBike = closestBike.second * DeliveryType.BIKE.getTimePerKilometer();
        timeToBike += restaurantToClientKilometers * DeliveryType.BIKE.getTimePerKilometer();

        if (timeToBike > timeToCar) {
            result.first = closestCar.first;
            result.second += closestCar.second;
        } else {
            result.first = closestBike.first;
            result.second += closestBike.second;
        }

        return result.second;
    }

    private int getDistanceWithCheapShippingMethod(Pair<MapEntity, Integer> result) {
        int priceToCar = closestCar.second * DeliveryType.CAR.getPricePerKilometer();
        priceToCar += restaurantToClientKilometers * DeliveryType.CAR.getPricePerKilometer();

        int priceToBike = closestBike.second * DeliveryType.BIKE.getPricePerKilometer();
        priceToBike += restaurantToClientKilometers * DeliveryType.BIKE.getPricePerKilometer();

        if (priceToBike > priceToCar) {
            result.first = closestCar.first;
            result.second += closestCar.second;
        } else {
            result.first = closestBike.first;
            result.second += closestBike.second;
        }
        return result.second;
    }

    /*
     * Returns pair of deliveryGuy and distance of the path
     */
    public Pair<MapEntity, Integer> getDeliveryGuyBasedOnCriteria(ShippingMethod shippingMethod) {
        Pair<MapEntity, Integer> result = new Pair<>();
        result.second = restaurantToClientKilometers;

        if (shippingMethod == ShippingMethod.FASTEST) {
            getDistanceWithFastShippingMethod(result);
        } else if (shippingMethod == ShippingMethod.CHEAPEST) {
            getDistanceWithCheapShippingMethod(result);
        } else {
            throw new UnsupportedOperationException("cannot have other type of shipping method");
        }
        return result;
    }
}

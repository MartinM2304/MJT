package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.Glovo;
import bg.sofia.uni.fmi.mjt.glovo.GlovoApi;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.PathFinder;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

public class ControlCenter implements ControlCenterApi {

    private GlovoApi glovo;
    private char[][] mapLayout;

    public ControlCenter(char[][] mapLayout) {
        if (mapLayout == null) {
            throw new IllegalArgumentException("mapLayout cannot be null");
        }
        this.mapLayout = mapLayout;
        // cannot return it from Glovo as it doesnt have the method getMapLayout, TODO add it if
        //you deside it is better
        glovo = new Glovo(mapLayout);
    }

    private Delivery getFastestDeliveryGuy(MapEntity client, MapEntity restaurant) {
        Delivery result;

        try {
            result= glovo.getFastestDelivery(client, restaurant, "Unknown");//the foodItem is not used
        } catch (NoAvailableDeliveryGuyException e) {
            System.out.println("There is no available deliveryGuy now, try again later");//if it was multitheradi easy solution, now ddz
            throw  new UnsupportedOperationException("getFastestDeliveryGuy no free guys");
        }

        return result;
    }

    private Delivery getFastestDeliveryWithMaxPrice(MapEntity client, MapEntity restaurant,double maxPrice) {
        Delivery result;
        try {
            result= glovo.getFastestDeliveryUnderPrice(client, restaurant, "Unknown",maxPrice);//the foodItem is not used
        } catch (NoAvailableDeliveryGuyException e) {
            System.out.println("There is no available deliveryGuy now, try again later");//if it was multitheradi easy solution, now ddz
            throw  new UnsupportedOperationException("getFastestDeliveryWithMaxPrice no free guys");
        }

        return result;
    }

    private Delivery getCheapestDelivery(MapEntity client,MapEntity restaurant){
        Delivery result;

        try {
            result=glovo.getCheapestDelivery(client,restaurant,"Unknown");
        }catch (NoAvailableDeliveryGuyException e){
            System.out.println("There is no available deliveryGuy now, try again later");//if it was multitheradi easy solution, now ddz
            throw new UnsupportedOperationException("Not implemented");
        }
        return result;
    }

    private Delivery getCheapestDeliveryGuyWithTimeRange(MapEntity client, MapEntity restaurant,int time) {
        Delivery result;
        try {
            result= glovo.getCheapestDeliveryWithinTimeLimit(client, restaurant, "Unknown",time);//the foodItem is not used
        } catch (NoAvailableDeliveryGuyException e) {
            System.out.println("There is no available deliveryGuy now, try again later");//if it was multitheradi easy solution, now ddz
            throw  new UnsupportedOperationException("getCheapestDeliveryGuyWithTimeRange no free guys");
        }

        return result;
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
        // if shipping=fast:
        //find fastest, if too expensive find fastest with maxPrice
        //if shippin=cheap
        //find cheapest, if too swol find cheapest with MaxTime
        //TODO ask what to do if there is no available guy
        MapEntity client = new MapEntity(clientLocation, MapEntityType.CLIENT);
        MapEntity restaurant = new MapEntity(restaurantLocation, MapEntityType.RESTAURANT);
        Delivery delivery=null;

        if (shippingMethod == ShippingMethod.FASTEST) {
            if(maxPrice==-1) {
                delivery = getFastestDeliveryGuy(client, restaurant);
            }else{
                delivery=getFastestDeliveryWithMaxPrice(client,restaurant,maxPrice);
            }
        }else if(shippingMethod==ShippingMethod.CHEAPEST){
            if(maxTime==-1){
                delivery=getCheapestDelivery(client,restaurant);
            }else {
                delivery=getCheapestDeliveryGuyWithTimeRange(client,restaurant,maxTime);
            }
        }else {
            throw new UnsupportedOperationException("cannot have other type of shipping method");
        }

        return new DeliveryInfo(delivery);
    }

    /**
     * Returns the map
     *
     * @return A MapEntity[][] containing the map
     */
    public MapEntity[][] getLayout() {
        //TODO
        int rows=mapLayout.length;
        int columns=mapLayout[0].length;
        MapEntity[][]layout=new MapEntity[rows][];
        for(int i=0;i<rows;i++){
            layout[i]=new MapEntity[columns];
            for(int j=0;j<columns;j++){
                layout[i][j]= PathFinder.getEntityFromLocation(new Location(i,j),mapLayout);
            }
        }
        return layout;
    }
}

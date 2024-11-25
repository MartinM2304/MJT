package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;

public class Delivery {

    private final Location client;
    private final Location restaurant;
    private Location deliveryGuy;
    //TODO currently not sure if food and price are const check in future
    private String foodItem;
    private double price;
    private int estimatedTime;
    private DeliveryType deliveryType;

    public Delivery(Location client, Location restaurant, Location deliveryGuy, String foodItem, double price, int estimatedTime) {
        validate(client, restaurant, deliveryGuy, foodItem, price, estimatedTime);
        this.client = client;
        this.restaurant = restaurant;
        this.deliveryGuy = deliveryGuy;
        this.foodItem = foodItem;
        this.price = price;
        this.estimatedTime = estimatedTime;
    }

    public Delivery(DeliveryInfo deliveryInfo, Location client, Location restaurant, String foodItem) {
        this(client, restaurant, deliveryInfo.deliveryGuyLocation(), foodItem, deliveryInfo.price(), deliveryInfo.estimatedTime());
        this.deliveryType = deliveryInfo.deliveryType();
    }

    private void validate(Location client, Location restaurant, Location deliveryGuy, String foodItem, double price, int estimated) {
        //TODO ask if shoul be in one if or more than 1
        if (client == null || restaurant == null || deliveryGuy == null) {
            throw new IllegalArgumentException("client/restaurant/deliveryGuy is null");
        }
        if (foodItem == null || foodItem.isBlank()) {
            throw new IllegalArgumentException("fooditem is null or empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("price cant be negative");
        }
        if (estimated < 0) {
            throw new IllegalArgumentException("estimated cant be negative");
        }
    }

    public double getPrice() {
        return price;
    }

    public Location getDeliveryGuyLocation() {
        return deliveryGuy;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        if (deliveryType == null) {
            throw new IllegalArgumentException("deliveryType is null");
        }
        this.deliveryType = deliveryType;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "client=" + client +
                ", restaurant=" + restaurant +
                ", deliveryGuy=" + deliveryGuy +
                ", foodItem='" + foodItem + '\'' +
                ", price=" + price +
                ", estimatedTime=" + estimatedTime +
                ", deliveryType=" + deliveryType +
                '}';
    }
}

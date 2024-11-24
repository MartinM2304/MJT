package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;

public record DeliveryInfo(Location deliveryGuyLocation, double price, int estimatedTime, DeliveryType deliveryType) {

    public DeliveryInfo{
        //TODO validate
    }

    public DeliveryInfo(Delivery delivery){
        this(delivery.getDeliveryGuyLocation(),delivery.getPrice(),delivery.getEstimatedTime(),delivery.getDeliveryType());
    }
}

package bg.sofia.uni.fmi.mjt.glovo.delivery;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;

public record DeliveryInfo(Location deliveryGuyLocation, double price, int estimatedTime, DeliveryType deliveryType) {

    public DeliveryInfo {
        validateDeliveryInfoData(deliveryGuyLocation, price, estimatedTime, deliveryType);
    }

    void validateDeliveryInfoData(Location deliveryGuyLocation, double price
            , int estimatedTime, DeliveryType deliveryType) {
        if (deliveryGuyLocation == null) {
            throw new IllegalArgumentException("deliveryGuys is  null");
        }
        if (deliveryType == null) {
            throw new IllegalArgumentException("deliveryType is null");
        }
        if (price < 0) {
            throw new IllegalArgumentException("price is less than 0");
        }
        if (estimatedTime < 1) {
            throw new IllegalArgumentException("time is zero or less");
        }
    }
}

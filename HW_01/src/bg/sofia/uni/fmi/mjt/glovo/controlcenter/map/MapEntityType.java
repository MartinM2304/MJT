package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;

public enum MapEntityType {
    ROAD('.'),
    WALL('#'),
    RESTAURANT('R'),
    CLIENT('C'),
    DELIVERY_GUY_CAR('A'),
    DELIVERY_GUY_BIKE('B');

    private final char entity;

    MapEntityType(char entity) {
        if (isValid(entity)) {
            this.entity = entity;
        } else {
            throw new IllegalArgumentException("entity is incorrect");
        }
    }

    private boolean isValid(char entity) {
        return switch (entity) {
            case '.', '#', 'R', 'C', 'A', 'B' -> true;
            default -> false;
        };
    }

    public char getEntity() {
        return entity;
    }

    public DeliveryType getDeliveryTypeIfCarOrBike() {
        if (this == DELIVERY_GUY_BIKE) {
            return DeliveryType.BIKE;
        } else if (this == DELIVERY_GUY_CAR) {
            return DeliveryType.CAR;
        } else {
            return null;
        }
    }

    public static MapEntityType fromChar(char ch) {
        for (MapEntityType type : values()) {
            if (type.entity == ch) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid char for conversion");
    }
}

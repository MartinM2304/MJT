package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import java.util.Set;

//TODO ask if enums should be tested
public enum MapEntityType {
    ROAD('.'),
    WALL('#'),
    RESTAURANT('R'),
    CLIENT('C'),
    DELIVERY_GUY_CAR('A'),
    DELIVERY_GUY_BIKE('B');

    private final char entity;

    MapEntityType(char entity) {
        //TODO remove later if no validation is needed
        if(Set.of('.','#','R','C','A','B').contains(entity)) {
            this.entity = entity;
        }else{
            throw new IllegalArgumentException("entity is incorrect");
        }
    }

    public char getEntity(){
        return entity;
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

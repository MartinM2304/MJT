package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public record Location(int x, int y) {

    public Location {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("x or y coordinate is smaller than 0");
        }
    }

    public List<Location> getNeighbors(int rows, int columns) {
        List<Location> result = new ArrayList<>();

        if (x > 0) {
            result.add(new Location(x - 1, y));
        }
        if (x < rows - 1) {
            result.add(new Location(x + 1, y));
        }
        if (y > 0) {
            result.add(new Location(x, y - 1));
        }
        if (y < columns - 1) {
            result.add(new Location(x, y + 1));
        }
        return result;
    }

    public static void validateLocationBasedOnMap(Location location, int limitX,int limitY){
        if(location.x>=limitX|| location.y>=limitY){
            throw new IllegalArgumentException("your coordinates are not valid");
        }
    }

    @Override
    public boolean equals(Object o){
        if(this==o){
            return true;
        }
        if(o==null|| getClass()!= o.getClass()){
            return false;
        }
        Location other= (Location) o;
        return (this.x==other.x&& this.y==other.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

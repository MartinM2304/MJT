package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
}

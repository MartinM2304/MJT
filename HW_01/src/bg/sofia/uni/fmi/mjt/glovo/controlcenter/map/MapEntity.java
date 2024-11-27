package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import java.util.List;
import java.util.Objects;

public record MapEntity(Location location, MapEntityType type) {

    public MapEntity {
        if (location == null || type == null) {
            throw new IllegalArgumentException("location or type is null");
        }
    }

    public List<Location> getNeighbors(int rows, int columns) {
        return location.getNeighbors(rows, columns);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MapEntity other = (MapEntity) o;
        return (this.location.equals(other.location) && this.type == other.type);

    }

    @Override
    public int hashCode() {
        return Objects.hash(location, type);
    }
}

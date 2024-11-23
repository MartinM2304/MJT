package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocationTest {

    @Test
    public void testWithValidCoordinates(){
        Location location= new Location(1,2);

        assertTrue(location.x()==1&&location.y()==2);
    }

    @Test
    public void testWithNegativeCoordinates(){
        assertThrows(IllegalArgumentException.class,()->new Location(-1,2));
    }
}

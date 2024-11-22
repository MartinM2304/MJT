package bg.sofia.uni.fmi.mjt.olympics.competitor;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AthleteTest {

    @Test
    void testConstructorWithNullID() {
        assertThrows(IllegalArgumentException.class, () -> new Athlete(null, "pepi", "bg"));
    }

    @Test
    void testConstructorWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> new Athlete("01", null, "bg"));
    }

    @Test
    void testConstructorWithNullNationality() {
        assertThrows(IllegalArgumentException.class, () -> new Athlete("02", "pepi", null));
    }

    @Test
    void testAddMedalWithNull() {
        Competitor cmp = new Athlete("01", "pepi", "bg");
        assertThrows(IllegalArgumentException.class, () -> cmp.addMedal(null));
    }

    @Test
    void testAddMedal() {
        Competitor cmp = new Athlete("01", "pepi", "bg");
        cmp.addMedal(Medal.GOLD);
        assertTrue(cmp.getMedals().contains(Medal.GOLD));
    }

    @Test
    void testAddTwoMedal() {
        Competitor cmp = new Athlete("01", "pepi", "bg");
        cmp.addMedal(Medal.BRONZE);
        cmp.addMedal(Medal.BRONZE);
        assertEquals(true, 2 == cmp.getMedals().size());
    }

    @Test
    void testGetMedalsReturnsUnmodifiable() {
        Athlete athlete = new Athlete("1", "Pepi", "srb");
        List<Medal> medals = (List<Medal>) athlete.getMedals();
        assertThrows(UnsupportedOperationException.class, () -> medals.add(Medal.GOLD), "Medals collection should be unmodifiable");
    }


}

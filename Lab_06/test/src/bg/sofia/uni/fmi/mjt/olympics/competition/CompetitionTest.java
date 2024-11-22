package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Executable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompetitionTest {


    @Test
    void testNameIsNull() {
        Set<Competitor>competitors=new HashSet<>();
        competitors.add(new Athlete("1","pepi","bg"));
        assertThrows(IllegalArgumentException.class,()->new  Competition(null,"100m",competitors));
    }
    @Test
    void testNameIsBlank() {
        Set<Competitor>competitors=new HashSet<>();
        competitors.add(new Athlete("1","pepi","bg"));
        assertThrows(IllegalArgumentException.class,()->new  Competition("","100m",competitors));
    }

    @Test
    void testDisciplineIsNull() {
        Set<Competitor>competitors=new HashSet<>();
        competitors.add(new Athlete("1","pepi","bg"));
        assertThrows(IllegalArgumentException.class,()->new  Competition("Running",null,competitors));
    }
    @Test
    void testDisciplineIsBlank() {
        Set<Competitor>competitors=new HashSet<>();
        competitors.add(new Athlete("1","pepi","bg"));
        assertThrows(IllegalArgumentException.class,()->new  Competition("Running","",competitors));
    }

    @Test
    void testcompetitorsIsNull() {
        assertThrows(IllegalArgumentException.class,()->new  Competition("Running","100m",null));
    }
    @Test
    void testcompetitorsIsEmpty() {
        Set<Competitor>competitors=new HashSet<>();
        assertThrows(IllegalArgumentException.class,()->new  Competition("Running","100m",competitors));
    }

    @Test
    void testEqualsIfTheyEqual() {
        Set<Competitor> nullSet = new HashSet<>();
        Competition cmp1 = new Competition("Running", "100m", nullSet);
        Competition cmp2 = new Competition("Running", "100m", null);

        assertEquals(true, cmp1.equals(cmp2));
    }

    @Test
    void testEqualsIfTheyNotEqual() {
        Set<Competitor> nullSet = new HashSet<>();
        Competition cmp1 = new Competition("Swiming", "100m", nullSet);
        Competition cmp2 = new Competition("Running", "100m", null);

        assertEquals(false, cmp1.equals(cmp2));
    }

//    @Test
//    void testEqualHashCodes(){
//        //TODO- myrzi me
//    }
}

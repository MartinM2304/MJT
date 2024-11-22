package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionTest;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MJTOlympicsTest {

    @Test
    void testUpdateMedalStatisticsWhenCompetitionNull(){
        MJTOlympics olympics= new MJTOlympics(null,new CompetitorResultFetcherStub());
        assertThrows(IllegalArgumentException.class,()->olympics.updateMedalStatistics(null));
    }

    @Test
    void testUpdateMedalStatisticsForUnregisteredCompetitor(){
        Set<Competitor>competitors=new HashSet<>();
        competitors.add(new Athlete("01","pepi","bg"));
        competitors.add(new Athlete("02","gogo","bgr"));

        Competition cmp=new Competition("swim","100",competitors);
        MJTOlympics olympics=new MJTOlympics(competitors,new CompetitorResultFetcherStub());

        Set<Competitor>competitors2=new HashSet<>();
        competitors2.add(new Athlete("03","niki","fr"));
        Competition cmp2= new Competition("run","20",competitors2);

        assertThrows(IllegalArgumentException.class,()-> olympics.updateMedalStatistics(cmp2));
    }

    @Test
    void testUpdateMedalStatisticsForSameCMP(){
        Set<Competitor>competitors=new HashSet<>();
        competitors.add(new Athlete("01","pepi","bg"));
        competitors.add(new Athlete("02","gogo","bgr"));
        Competition cmp=new Competition("swim","100",competitors);
        MJTOlympics olympics=new MJTOlympics(competitors,new CompetitorResultFetcherStub());
        assertDoesNotThrow(()->olympics.updateMedalStatistics(cmp));
    }

    @Test
    void testGetNationsRankListSortsCorrectly() {
    }
//        Competitor competitor1 = mock(Athlete.class);
//        when(competitor1.getNationality()).thenReturn("USA");
//        when(competitor1.getMedals()).thenReturn(List.of(Medal.GOLD));
//
//        Competitor competitor2 = mock(Athlete.class);
//        when(competitor2.getNationality()).thenReturn("Canada");
//        when(competitor2.getMedals()).thenReturn(List.of(Medal.SILVER, Medal.BRONZE));
//
//        olympics.getRegisteredCompetitors().addAll(Set.of(competitor1, competitor2));
//
//        TreeSet<String> nations = olympics.getNationsRankList();
//        assertEquals(List.of("Canada", "USA"), new ArrayList<>(nations),
//                "Expected nations rank list to be sorted by medal count");
//    }
}

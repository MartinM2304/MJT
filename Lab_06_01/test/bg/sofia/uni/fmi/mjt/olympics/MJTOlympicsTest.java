package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.CompetitorResultFetcherStub;
import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;
import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MJTOlympicsTest {

    private  TreeSet<Competitor> createSortedResult(Competition cmp){
        TreeSet<Competitor>result= new TreeSet<>(new Comparator<Competitor>() {
            @Override
            public int compare(Competitor o1, Competitor o2) {
                return Integer.compare(o1.getMedals().size(),o2.getMedals().size());
            }
        });
        result.addAll(cmp.competitors());
        return result;
    }

    private CompetitionResultFetcher getMock(Competition cmp){
        CompetitionResultFetcher result = mock();
        when(result.getResult(cmp)).thenReturn(createSortedResult(cmp));
        return result;
    }
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
        Competitor competitor1 = mock(Athlete.class);
        when(competitor1.getNationality()).thenReturn("USA");
        when(competitor1.getMedals()).thenReturn(List.of(Medal.GOLD));

        Competitor competitor2 = mock(Athlete.class);
        when(competitor2.getNationality()).thenReturn("Canada");
        when(competitor2.getMedals()).thenReturn(List.of(Medal.SILVER, Medal.BRONZE));

        MJTOlympics olympics = new MJTOlympics(Set.of(competitor1,competitor2),new CompetitorResultFetcherStub());

        TreeSet<String> nations = olympics.getNationsRankList();
        assertEquals(List.of("Canada", "USA"), new ArrayList<>(nations),
                "Expected nations rank list to be sorted by medal count");
    }
}

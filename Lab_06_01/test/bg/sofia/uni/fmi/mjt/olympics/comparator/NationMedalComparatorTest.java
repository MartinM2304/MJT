package bg.sofia.uni.fmi.mjt.olympics.comparator;

import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;
import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import bg.sofia.uni.fmi.mjt.olympics.MJTOlympicsTest;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NationMedalComparatorTest {

    @Test
    void testCompareForBigger() {
        //MJTOlympics olympics = new MJTOlympics();
    }

    @Test
    void testCompareForSmaller() {

    }

    @Test
    void testCompareForEquals() {

    }

    @Test
    void compareSameMedalCounts() {
        Competitor athlete1 = new Athlete("01", "pepi", "bg");
        Competitor athlete2 = new Athlete("02", "gosho", "srb");
        Competitor athlete3 = new Athlete("03", "gosho", "bg");

        athlete1.addMedal(Medal.GOLD);
        athlete2.addMedal(Medal.GOLD);

        Set<Competitor> competitors = Set.of(athlete1, athlete2, athlete3);
        Competition competition = new Competition("walking", "Running", competitors);

        CompetitionResultFetcher resultFetcher = mock(CompetitionResultFetcher.class);
        when(resultFetcher.getResult(competition)).thenReturn(MJTOlympicsTest.createSortedResult(competition));

        MJTOlympics olympics = new MJTOlympics(competitors, resultFetcher);
        olympics.updateMedalStatistics(competition);

        TreeSet<String> result = olympics.getNationsRankList();
        assertTrue(result.first().equals("bg"));
    }
}

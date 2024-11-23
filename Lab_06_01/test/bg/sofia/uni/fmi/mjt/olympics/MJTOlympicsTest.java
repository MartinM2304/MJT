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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MJTOlympicsTest {

    private final CompetitionResultFetcher fetcher = mock(CompetitionResultFetcher.class);
    private final Competitor athlete1 = new Athlete("01", "cpepi", "bg");
    private final Competitor athlete2 = new Athlete("02", "bniki", "srb");
    private final Competitor athlete3 = new Athlete("03", "asasho", "bg");
    private final Set<Competitor> competitors = Set.of(athlete1, athlete2, athlete3);
    private final MJTOlympics mjtOlympics = new MJTOlympics(competitors, fetcher);

    public static TreeSet<Competitor> createSortedResult(Competition cmp) {
        TreeSet<Competitor> result = new TreeSet<>(new Comparator<Competitor>() {
            @Override
            public int compare(Competitor o1, Competitor o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        result.addAll(cmp.competitors());
        return result;
    }

    private CompetitionResultFetcher getMock(Competition cmp) {
        CompetitionResultFetcher result = mock();
        when(result.getResult(cmp)).thenReturn(createSortedResult(cmp));
        return result;
    }

    @Test
    void testUpdateMedalStatisticsUpdatesCorrectly() {
        Competition competition = new Competition("Olympic Race", "Running", competitors);
        TreeSet<Competitor> ranking = new TreeSet<>(new CustomComparator());
        ranking.add(athlete1);
        ranking.add(athlete2);

        when(fetcher.getResult(competition)).thenReturn(ranking);

        mjtOlympics.updateMedalStatistics(competition);

        assertEquals(1, athlete1.getMedals().size());
        assertTrue(athlete1.getMedals().contains(Medal.GOLD));// TODO
        assertTrue(athlete2.getMedals().contains(Medal.SILVER));
    }

    @Test
    void testUpdateMedalStatisticsWhenCompetitionNull() {
        MJTOlympics olympics = new MJTOlympics(null, new CompetitorResultFetcherStub());
        assertThrows(IllegalArgumentException.class, () -> olympics.updateMedalStatistics(null));
    }

    @Test
    void testUpdateMedalStatisticsForUnregisteredCompetitor() {
        Competition cmp = new Competition("run", "walk", Set.of(new Athlete("04", "dddz", "bg")));
        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.updateMedalStatistics(cmp));
    }

    @Test
    void testUpdateMedalStatisticsForSameCMP() {
        Set<Competitor> competitors = new HashSet<>();
        competitors.add(new Athlete("01", "pepi", "bg"));
        competitors.add(new Athlete("02", "gogo", "bgr"));
        Competition cmp = new Competition("swim", "100", competitors);
        MJTOlympics olympics = new MJTOlympics(competitors, new CompetitorResultFetcherStub());
        assertDoesNotThrow(() -> olympics.updateMedalStatistics(cmp));
    }

    @Test
    void testGetTotalMedalsWorksCorrectly() {
        athlete1.addMedal(Medal.GOLD);
        athlete3.addMedal(Medal.SILVER);
        Competition cmp = new Competition("run", "walk", Set.of(athlete1, athlete3));
        when(fetcher.getResult(cmp)).thenReturn(MJTOlympicsTest.createSortedResult(cmp));
        mjtOlympics.updateMedalStatistics(cmp);

        assertEquals(2, mjtOlympics.getTotalMedals("bg"));
    }

    @Test
    void testGetTotalMedalsThrowsForNonExistentNationality() {
        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.getTotalMedals("Unknown"));
    }

    @Test
    void testGetNationsRankListSortsCorrectly() {
        athlete1.addMedal(Medal.GOLD);
        athlete2.addMedal(Medal.BRONZE);
        Competition cmp = new Competition("run", "walk", Set.of(athlete1, athlete3));
        when(fetcher.getResult(cmp)).thenReturn(MJTOlympicsTest.createSortedResult(cmp));
        mjtOlympics.updateMedalStatistics(cmp);
        TreeSet<String> nationsRankList = mjtOlympics.getNationsRankList();

        assertEquals("bg", nationsRankList.first());
    }

    @Test
    void getAthleteWithGoldFirst() {
        athlete1.addMedal(Medal.GOLD);
        athlete2.addMedal(Medal.GOLD);
        Set<Competitor> competitors = Set.of(athlete1, athlete2);
        Competition cmp = new Competition("Sprint", "Running", competitors);
        when(fetcher.getResult(cmp)).thenReturn(MJTOlympicsTest.createSortedResult(cmp));
        mjtOlympics.updateMedalStatistics(cmp);


        TreeSet<String> result = mjtOlympics.getNationsRankList();
        assertEquals("bg", result.first());
    }

}

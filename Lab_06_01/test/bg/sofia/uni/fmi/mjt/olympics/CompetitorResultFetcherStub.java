package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;

import java.util.Comparator;
import java.util.TreeSet;

public class CompetitorResultFetcherStub implements CompetitionResultFetcher {

    /**
     * Fetches the results of a given competition.
     * The result is a ranking leaderboard that orders the Competitors by their performance in the competition.
     *
     * @param competition the competition to fetch the results from
     * @return a TreeSet of competitors ranked by their performance in the competition
     */
    @Override
    public TreeSet<Competitor> getResult(Competition competition){
        TreeSet<Competitor>result= new TreeSet<>(new Comparator<Competitor>() {
            @Override
            public int compare(Competitor o1, Competitor o2) {
                return Integer.compare(o1.getMedals().size(),o2.getMedals().size());
            }
        });
        result.addAll(competition.competitors());
        return result;
    }
}

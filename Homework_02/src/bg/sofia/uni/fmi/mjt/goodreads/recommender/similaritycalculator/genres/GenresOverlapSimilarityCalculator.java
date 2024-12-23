package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.HashSet;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.goodreads.book.utill.genres.GenresWrapper.overlapCoefficient;

public class GenresOverlapSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("first or secondd is null");
        }
        List<String> genresFirst = first.genres();
        List<String> genresSecond = second.genres();
        return overlapCoefficient(new HashSet<String>(genresFirst), new HashSet<String>(genresSecond));
    }

}

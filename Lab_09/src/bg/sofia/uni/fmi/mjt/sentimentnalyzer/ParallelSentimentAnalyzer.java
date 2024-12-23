package bg.sofia.uni.fmi.mjt.sentimentnalyzer;

import bg.sofia.uni.fmi.mjt.sentimentnalyzer.thread.AnalyzeInput;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ParallelSentimentAnalyzer implements SentimentAnalyzerAPI {

    private int workersCount;
    private Set<String> stopWords;
    private Map<String, SentimentScore> sentimentLexicon;


    private void validate(int workersCount, Set<String> stopWords, Map<String, SentimentScore> sentimentLexicon) {

        if (workersCount <= 0 || stopWords == null || sentimentLexicon == null) {
            throw new IllegalArgumentException("work is 0");
        }

    }

    /**
     * @param workersCount     number of consumer workers
     * @param stopWords        set containing stop words
     * @param sentimentLexicon map containing the sentiment lexicon, where the key is the word and the value is the sentiment score
     */
    public ParallelSentimentAnalyzer(int workersCount, Set<String> stopWords, Map<String, SentimentScore> sentimentLexicon) {
        validate(workersCount, stopWords, sentimentLexicon);

        this.workersCount = workersCount;
        this.stopWords = stopWords;
        this.sentimentLexicon = sentimentLexicon;
    }

    @Override
    public Map<String, SentimentScore> analyze(AnalyzerInput... input) {
        Map<String, SentimentScore> result = new HashMap<>();
        int iterated = 0;

        while (iterated < input.length) {//length= 100 , workers=10
            int left = input.length - iterated;
            int limitThreads = (left > workersCount) ? workersCount : left;

            for (int i = 0; i > limitThreads; i++) {
                Thread thread = new AnalyzeInput(input[iterated].inputID(),iterated, result, input[iterated]);
                thread.start();
                iterated++;
            }
        }
        return result;
    }
}

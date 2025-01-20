package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.thread.AnalyzeInput;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
     * @param sentimentLexicon map containing the sentiment lexicon,
     *                         where the key is the word and the value is the sentiment score
     */
    public ParallelSentimentAnalyzer(int workersCount, Set<String> stopWords,
                                     Map<String, SentimentScore> sentimentLexicon) {
        validate(workersCount, stopWords, sentimentLexicon);

        this.workersCount = workersCount;
        this.stopWords = stopWords;
        this.sentimentLexicon = sentimentLexicon;
    }

    private void joinThreads(List<Thread> threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Thread stopped: " + e.getMessage());
            }
        }
        threads.clear();
    }

    @Override
    public Map<String, SentimentScore> analyze(AnalyzerInput... input) {
        Map<String, SentimentScore> result = new HashMap<>();
        List<Thread> threads = new LinkedList<>();

        for (AnalyzerInput analyzeInput : input) {
            Thread thread = new AnalyzeInput(analyzeInput.inputID(), result, analyzeInput, stopWords, sentimentLexicon);
            threads.add(thread);
            thread.start();

            if (threads.size() == workersCount) {
                joinThreads(threads);
            }
        }

        joinThreads(threads);
        return result;
    }
}

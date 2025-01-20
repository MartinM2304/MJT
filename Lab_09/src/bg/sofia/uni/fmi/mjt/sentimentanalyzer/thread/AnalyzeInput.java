package bg.sofia.uni.fmi.mjt.sentimentanalyzer.thread;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.AnalyzerInput;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentScore;

import java.io.BufferedReader;
import java.util.Map;
import java.util.Set;

public class AnalyzeInput extends Thread {
    private String inputID;
    private AnalyzerInput input;
    private Map<String, SentimentScore> data;
    private Set<String> stopWords;
    private Map<String, SentimentScore> sentimentLexicon;

    public AnalyzeInput(String inputID, Map<String, SentimentScore> data,
                        AnalyzerInput input, Set<String> stopWords,
                        Map<String, SentimentScore> sentimentLexicon) {
        if (inputID == null || input == null || data == null) {
            throw new IllegalArgumentException("null");
        }
        this.sentimentLexicon = sentimentLexicon;
        this.stopWords = stopWords;
        this.inputID = inputID;
        this.input = input;
        this.data = data;
    }

    @Override
    public void run() {
        int result = 0;
        try (BufferedReader bufferedReader = new BufferedReader(input.inputReader())) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.toLowerCase();
                line = line.replaceAll("\\p{Punct}", "").replaceAll("\\s+", " ").trim();

                result = java.util.Arrays.stream(line.split("\\s+"))
                        .filter(word -> !stopWords.contains(word))
                        .mapToInt(word -> (sentimentLexicon.getOrDefault(word, SentimentScore.NEUTRAL)).getScore())
                        .sum();

            }

        } catch (Exception e) {
            System.err.println("Error processing input: " + e.getMessage());
        }

        synchronized (data) {
            data.put(inputID, SentimentScore.fromScore(result));
        }
    }
}

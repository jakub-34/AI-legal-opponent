package legalopponent.repository;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;

import java.util.ArrayList;
import java.util.List;

public class TextAnalyzer {

    private static final int MIN_TOKEN_LENGTH = 3;
    private static final double SIMILARITY_THRESHOLD = 0.85;

    private final JaroWinklerSimilarity similarityCalculator;

    public TextAnalyzer() {
        this.similarityCalculator = new JaroWinklerSimilarity();
    }

    public List<String> tokenize(String text) {
        String cleaned = text.replaceAll("[,.:;!?()\\[\\]{}\"']", " ");
        String[] words = cleaned.split("\\s+");

        List<String> tokens = new ArrayList<>();
        for (String word : words) {
            String trimmed = word.trim();
            if (trimmed.length() > MIN_TOKEN_LENGTH) {
                tokens.add(trimmed);
            }
        }
        return tokens;
    }

    public boolean areSimilar(String term1, String term2) {
        double similarity = similarityCalculator.apply(term1, term2);
        return similarity >= SIMILARITY_THRESHOLD;
    }
}

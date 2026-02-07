package legalopponent.repository;

import legalopponent.domain.LegalDocument;

import java.util.List;

public class RelevanceScorer {

    private static final double EXACT_MATCH_WEIGHT = 2.0;
    private static final double PARTIAL_MATCH_WEIGHT = 0.5;

    private final TextAnalyzer textAnalyzer;

    public RelevanceScorer(TextAnalyzer textAnalyzer) {
        this.textAnalyzer = textAnalyzer;
    }

    public double calculateScore(LegalDocument doc, List<String> queryTerms) {
        String contentLower = doc.content().toLowerCase();
        List<String> docTerms = textAnalyzer.tokenize(contentLower);

        double score = 0.0;

        if (docTerms.isEmpty() || queryTerms.isEmpty()) {
            return score;
        }

        for (String queryTerm : queryTerms) {
            if (contentLower.contains(queryTerm)) {
                score += EXACT_MATCH_WEIGHT;
            }

            for (String docTerm : docTerms) {
                if (textAnalyzer.areSimilar(queryTerm, docTerm)) {
                    score += PARTIAL_MATCH_WEIGHT;
                }
            }
        }

        return score / queryTerms.size();
    }
}

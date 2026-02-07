package legalopponent.repository;

import legalopponent.domain.LegalDocument;

record DocumentScore(LegalDocument document, double score) implements Comparable<DocumentScore> {

    @Override
    public int compareTo(DocumentScore other) {
        return Double.compare(other.score, this.score);
    }
}

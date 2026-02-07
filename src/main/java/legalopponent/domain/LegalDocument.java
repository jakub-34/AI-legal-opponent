package legalopponent.domain;

public record LegalDocument(
        String id,
        String name,
        DocumentType type,
        String content
) {
    public enum DocumentType {
        ZAKON,
        JUDIKAT
    }
}

package legalopponent.service;

import legalopponent.domain.LegalDocument;

import java.util.List;

public class ContextBuilder {

    public String buildContext(List<LegalDocument> documents) {
        if (documents.isEmpty()) {
            return "";
        }

        StringBuilder context = new StringBuilder();
        context.append("RELEVANTNÉ PRÁVNE DOKUMENTY:\n\n");

        for (LegalDocument doc : documents) {
            context.append("=== ").append(doc.name()).append(" ===\n");
            context.append("Typ: ").append(formatType(doc.type())).append("\n\n");
            context.append(doc.content()).append("\n\n");
        }

        return context.toString();
    }

    private String formatType(LegalDocument.DocumentType type) {
        return switch (type) {
            case ZAKON -> "Zákon";
            case JUDIKAT -> "Judikát";
        };
    }
}

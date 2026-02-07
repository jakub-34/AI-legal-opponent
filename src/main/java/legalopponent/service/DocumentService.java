package legalopponent.service;

import legalopponent.domain.LegalDocument;
import legalopponent.repository.DocumentRepository;

import java.util.List;

public class DocumentService {

    private final DocumentRepository repository;
    private final ContextBuilder contextBuilder;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
        this.contextBuilder = new ContextBuilder();
    }

    public List<LegalDocument> getAllDocuments() {
        return repository.findAll();
    }

    public List<LegalDocument> findRelevantDocuments(String query) {
        return repository.findRelevant(query);
    }

    public String buildContext(List<LegalDocument> documents) {
        return contextBuilder.buildContext(documents);
    }
}

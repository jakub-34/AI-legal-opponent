package legalopponent.repository;

import legalopponent.domain.LegalDocument;

import java.util.List;

public interface DocumentRepository {
    List<LegalDocument> findAll();
    List<LegalDocument> findRelevant(String query);
}

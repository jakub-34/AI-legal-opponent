package legalopponent.repository;

import legalopponent.domain.LegalDocument;
import legalopponent.domain.LegalDocument.DocumentType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileDocumentRepository implements DocumentRepository {

    private final Path dataDirectory;
    private List<LegalDocument> documents;
    private final TextAnalyzer textAnalyzer;
    private final RelevanceScorer relevanceScorer;

    public FileDocumentRepository(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.textAnalyzer = new TextAnalyzer();
        this.relevanceScorer = new RelevanceScorer(textAnalyzer);
    }

    @Override
    public List<LegalDocument> findAll() {
        if (documents == null) {
            documents = loadDocuments();
        }
        return documents;
    }

    @Override
    public List<LegalDocument> findRelevant(String query) {
        String queryLower = query.toLowerCase();
        List<String> queryTerms = textAnalyzer.tokenize(queryLower);

        if (queryTerms.isEmpty()) {
            return List.of();
        }

        return findAll().stream()
                .map(doc -> new DocumentScore(doc, relevanceScorer.calculateScore(doc, queryTerms)))
                .filter(ds -> ds.score() > 0)
                .sorted()
                .map(DocumentScore::document)
                .toList();
    }


    private List<LegalDocument> loadDocuments() {
        List<LegalDocument> loaded = new ArrayList<>();

        try (var stream = Files.list(dataDirectory)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt"))
                    .forEach(path -> {
                        try {
                            String content = Files.readString(path);
                            String fileName = path.getFileName().toString();
                            DocumentType type = determineType(fileName);

                            loaded.add(new LegalDocument(
                                    fileName,
                                    formatName(type),
                                    type,
                                    content
                            ));
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to load document: " + path, e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read data directory", e);
        }

        return loaded;
    }

    private DocumentType determineType(String fileName) {
        if (fileName.contains("judikat")) {
            return DocumentType.JUDIKAT;
        }
        return DocumentType.ZAKON;
    }

    private String formatName(DocumentType type) {
        return switch (type) {
            case JUDIKAT -> "Judikát - Spoločný nájom manželov";
            case ZAKON -> "Občiansky zákonník - Nájom";
        };
    }
}

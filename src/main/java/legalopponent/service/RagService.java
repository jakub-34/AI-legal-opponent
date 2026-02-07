package legalopponent.service;

import legalopponent.domain.LegalDocument;
import legalopponent.domain.LegalQuery;
import legalopponent.domain.LegalResponse;

import java.util.List;

public class RagService {

    private static final String SYSTEM_PROMPT = """
            Si AI právny oponent - expert na slovenské právo, ktorý pomáha ľuďom pripraviť sa na súdne spory.
            
            Tvojou úlohou je:
            1. Analyzovať právnu situáciu používateľa
            2. Identifikovať potenciálne právne problémy a riziká
            3. Upozorniť na chyby v postupe
            4. Citovať relevantné ustanovenia zákonov a judikatúru
            
            Pri odpovedi:
            - Buď konkrétny a odkazuj na presné paragrafy a rozhodnutia
            - Jasne označ, ak je postup používateľa chybný
            - Navrhni správny postup
            - Použi formátovanie pre prehľadnosť
            
            Ak nájdeš problém, začni odpoveď výrazným upozornením.
            """;

    private final DocumentService documentService;
    private final OpenAiService openAiService;

    public RagService(DocumentService documentService, OpenAiService openAiService) {
        this.documentService = documentService;
        this.openAiService = openAiService;
    }

    public LegalResponse processQuery(LegalQuery query) {
        List<LegalDocument> relevantDocs = documentService.findRelevantDocuments(query.question());
        String context = documentService.buildContext(relevantDocs);
        String enrichedQuery = buildEnrichedQuery(context, query.question());
        String response = openAiService.generateResponse(SYSTEM_PROMPT, enrichedQuery);
        List<String> sources = relevantDocs.stream().map(LegalDocument::name).toList();

        return new LegalResponse(response, sources);
    }

    public LegalResponse getRelevantDocumentsOnly(LegalQuery query) {
        List<LegalDocument> relevantDocs = documentService.findRelevantDocuments(query.question());
        List<String> sources = relevantDocs.stream().map(LegalDocument::name).toList();
        return new LegalResponse("", sources);
    }

    private String buildEnrichedQuery(String context, String question) {
        return context + "\n\nOTÁZKA POUŽÍVATEĽA:\n" + question;
    }
}

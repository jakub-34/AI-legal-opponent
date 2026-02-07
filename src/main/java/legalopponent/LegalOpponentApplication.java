package legalopponent;

import legalopponent.config.OpenAiConfig;
import legalopponent.domain.LegalQuery;
import legalopponent.domain.LegalResponse;
import legalopponent.repository.DocumentRepository;
import legalopponent.repository.FileDocumentRepository;
import legalopponent.service.DocumentService;
import legalopponent.service.OpenAiService;
import legalopponent.service.RagService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class LegalOpponentApplication {

    private static final String PROMPT = "\nVaša otázka (alebo 'exit' pre ukončenie):\n> ";

    public static void main(String[] args) {
        try {
            RagService ragService = initializeServices();
            runInteractiveMode(ragService);
        } catch (Exception e) {
            System.err.println("Chyba: " + e.getMessage());
            System.exit(1);
        }
    }

    private static RagService initializeServices() {
        Path dataPath = Path.of("data");
        OpenAiConfig config = OpenAiConfig.fromEnvironment();

        DocumentRepository repository = new FileDocumentRepository(dataPath);
        DocumentService documentService = new DocumentService(repository);
        OpenAiService openAiService = new OpenAiService(config);

        System.out.println("Načítané právne dokumenty z: " + dataPath.toAbsolutePath());
        System.out.println("Pripojené k OpenAI (" + config.model() + ")");

        return new RagService(documentService, openAiService);
    }

    private static void runInteractiveMode(RagService ragService) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.print(PROMPT);
                String input = reader.readLine();

                if (input == null || "exit".equalsIgnoreCase(input.trim())) {
                    System.out.println("\nDovidenia!");
                    break;
                }

                if (input.trim().isEmpty()) {
                    continue;
                }

                System.out.println("\nAnalyzujem vašu situáciu...\n");

                LegalQuery query = new LegalQuery(input);

                try {
                    LegalResponse response = ragService.processQuery(query);
                    printResponse(response);
                } catch (Exception aiError) {
                    System.err.println("Chyba pri komunikácii s AI: " + aiError.getMessage());

                    try {
                        LegalResponse partialResponse = ragService.getRelevantDocumentsOnly(query);
                        System.out.println("\n" + "═".repeat(60));
                        if (!partialResponse.relevantSources().isEmpty()) {
                            System.out.println("\nNašiel som relevantné právne dokumenty:");
                            partialResponse.relevantSources().forEach(source -> System.out.println("   • " + source));
                        }
                        else {
                            System.out.println("\nNenašiel som žiadne relevantné dokumenty.");
                        }
                        System.out.println("\n" + "═".repeat(60));
                    } catch (Exception docError) {
                        // Ignore document retrieval errors
                    }
                }

            } catch (Exception e) {
                System.err.println("Chyba pri spracovaní: " + e.getMessage());
            }
        }
    }

    private static void printResponse(LegalResponse response) {
        System.out.println("═".repeat(60));
        System.out.println("\n" + response.answer() + "\n");
        System.out.println("═".repeat(60));

        if (!response.relevantSources().isEmpty()) {
            System.out.println("\nPoužité zdroje:");
            response.relevantSources().forEach(source -> System.out.println("   • " + source));
        }
    }
}

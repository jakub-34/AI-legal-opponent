package legalopponent.config;

import io.github.cdimascio.dotenv.Dotenv;

public record OpenAiConfig(
        String apiKey,
        String model
) {
    public static OpenAiConfig fromEnvironment() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        String apiKey = dotenv.get("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Nastav svoj OPENAI_API_KEY v súbore .env");
        }

        String model = "o3-mini";

        return new OpenAiConfig(apiKey, model);
    }
}

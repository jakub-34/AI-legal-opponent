package legalopponent.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import legalopponent.config.OpenAiConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class OpenAiService {

    private static final String API_URL = "https://api.openai.com/v1/responses";

    private final OpenAiConfig config;
    private final HttpClient httpClient;
    private final Gson gson;

    public OpenAiService(OpenAiConfig config) {
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(60))
                .build();
        this.gson = new Gson();
    }

    public String generateResponse(String systemPrompt, String userMessage) {
        try {
            JsonObject requestBody = buildRequestBody(systemPrompt, userMessage);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + config.apiKey())
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                    .timeout(Duration.ofMinutes(5))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("OpenAI API error: " + response.statusCode() + " - " + response.body());
            }

            return extractResponse(response.body());
        } catch (Exception e) {
            throw new RuntimeException("Failed to call OpenAI API: " + e.getMessage(), e);
        }
    }

    private JsonObject buildRequestBody(String systemPrompt, String userMessage) {
        JsonObject body = new JsonObject();
        body.addProperty("model", "o3-mini");

        JsonArray input = new JsonArray();

        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "developer");
        systemMessage.addProperty("content", systemPrompt);
        input.add(systemMessage);

        JsonObject userMessageObj = new JsonObject();
        userMessageObj.addProperty("role", "user");
        userMessageObj.addProperty("content", userMessage);
        input.add(userMessageObj);

        body.add("input", input);

        JsonObject reasoning = new JsonObject();
        reasoning.addProperty("effort", "high");
        body.add("reasoning", reasoning);

        return body;
    }

    private String extractResponse(String responseBody) {
        JsonObject response = gson.fromJson(responseBody, JsonObject.class);
        JsonArray output = response.getAsJsonArray("output");

        for (int i = 0; i < output.size(); i++) {
            JsonObject item = output.get(i).getAsJsonObject();
            if ("message".equals(item.get("type").getAsString())) {
                JsonArray content = item.getAsJsonArray("content");
                if (!content.isEmpty()) {
                    return content.get(0).getAsJsonObject().get("text").getAsString();
                }
            }
        }

        throw new RuntimeException("No response content found in: " + responseBody);
    }
}

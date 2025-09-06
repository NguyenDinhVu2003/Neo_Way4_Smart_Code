package security.instagram.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OllamaService {

    @Qualifier("ollamaWebClient")
    private final WebClient webClient;

    @Value("${llm.api.model}")
    private String defaultModel;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Flux<String> streamChat(String prompt, String model) {
        // Sử dụng model mặc định nếu không được chỉ định
        String selectedModel = (model != null && !model.isEmpty()) ? model : defaultModel;

        // Tạo request body theo định dạng Gemini API
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        return webClient.post()
                .uri("/" + selectedModel + ":generateContent")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> log.error("Error calling Gemini API: {}", error.getMessage()))
                .onErrorResume(error -> {
                    log.error("Failed to connect to Gemini API. Error: {}", error.getMessage());
                    return Mono.just("Error: Cannot connect to Gemini API. Please check your API key and internet connection.");
                })
                .map(this::extractGeminiContent)
                .filter(content -> content != null && !content.isEmpty())
                .flatMapMany(content -> Flux.just(content)); // Trả về một lần thay vì streaming
    }

    private String extractGeminiContent(String response) {
        try {
            if (response.trim().isEmpty()) {
                return "No response from Gemini API";
            }

            JsonNode jsonResponse = objectMapper.readTree(response);
            JsonNode candidates = jsonResponse.get("candidates");

            if (candidates != null && candidates.isArray() && candidates.size() > 0) {
                JsonNode content = candidates.get(0).get("content");
                if (content != null) {
                    JsonNode parts = content.get("parts");
                    if (parts != null && parts.isArray() && parts.size() > 0) {
                        JsonNode text = parts.get(0).get("text");
                        if (text != null) {
                            return text.asText();
                        }
                    }
                }
            }

            // Nếu không tìm thấy content, trả về raw response để debug
            return "Gemini response: " + response;
        } catch (Exception e) {
            log.error("Error parsing Gemini response: {}, error: {}", response, e.getMessage());
            return "Error parsing response: " + e.getMessage();
        }
    }
}

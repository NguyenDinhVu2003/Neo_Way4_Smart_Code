package security.instagram.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import security.instagram.dto.OllamaResponse;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OllamaService {

    @Qualifier("ollamaWebClient")
    private final WebClient webClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Flux<String> streamChat(String prompt, String model) {
        Map<String, Object> requestBody = Map.of(
            "model", model,
            "messages", List.of(
                Map.of("role", "user", "content", prompt)
            ),
            "stream", true
        );

        return webClient.post()
                .uri("/api/chat")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnError(error -> log.error("Error calling Ollama API: {}", error.getMessage()))
                .onErrorResume(error -> {
                    log.error("Failed to connect to Ollama at localhost:11434. Make sure Ollama is running.");
                    return Flux.just("Error: Cannot connect to Ollama. Please ensure Ollama is running on localhost:11434");
                })
                .map(this::extractContent)
                .filter(content -> content != null && !content.isEmpty())
                .reduce("", (accumulated, newContent) -> {
                    if (accumulated.isEmpty()) {
                        return newContent;
                    }
                    return accumulated + " " + newContent;
                })
                .flux();
    }

    private String extractContent(String line) {
        try {
            if (line.trim().isEmpty()) {
                return "";
            }

            OllamaResponse response = objectMapper.readValue(line, OllamaResponse.class);
            if (response.getMessage() != null && response.getMessage().getContent() != null) {
                return response.getMessage().getContent();
            }
        } catch (Exception e) {
            log.error("Error parsing Ollama response line: {}, error: {}", line, e.getMessage());
        }
        return "";
    }
}

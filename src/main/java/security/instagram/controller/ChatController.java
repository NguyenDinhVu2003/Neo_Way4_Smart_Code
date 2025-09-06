package security.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import security.instagram.dto.ChatRequest;
import security.instagram.service.OllamaService;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final OllamaService ollamaService;

    @PostMapping(value = "/stream", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> streamChat(@RequestBody ChatRequest request) {
        return ollamaService.streamChat(request.getPrompt(), request.getModel())
                .reduce("", (accumulated, newContent) -> {
                    if (accumulated.isEmpty()) {
                        return newContent;
                    }
                    return accumulated + " " + newContent;
                });
    }
}

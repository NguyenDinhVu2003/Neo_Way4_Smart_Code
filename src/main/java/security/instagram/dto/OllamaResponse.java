package security.instagram.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OllamaResponse {
    private String model;
    private String created_at;
    private OllamaMessage message;
    private Boolean done;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OllamaMessage {
        private String role;
        private String content;
    }
}

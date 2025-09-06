package security.instagram.config;

import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {


    @Bean
    public WebClient ollamaWebClient() {
        return WebClient.builder()
                .baseUrl("https://6e515f6bdd77.ngrok-free.app")
                .build();
    }
}

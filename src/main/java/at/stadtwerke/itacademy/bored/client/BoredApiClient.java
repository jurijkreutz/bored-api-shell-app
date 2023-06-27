package at.stadtwerke.itacademy.bored.client;

import at.stadtwerke.itacademy.bored.model.Activity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BoredApiClient {

    private final WebClient webClient;

    public BoredApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.boredapi.com/api/").build();
    }

    public Mono<Activity> getSimpleActivity() {
        return webClient.get()
                .uri("/activity").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Activity.class);
    }
}

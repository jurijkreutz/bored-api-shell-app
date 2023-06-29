package at.stadtwerke.itacademy.bored.client;

import at.stadtwerke.itacademy.bored.model.Activity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
public class BoredApiClient {

    private final WebClient webClient;

    public BoredApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Activity> getSimpleActivity() {
        return webClient.get()
                .uri("/activity").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Activity.class);
    }

    public Mono<Activity> getActivityByType(String type) {
        String uriString = UriComponentsBuilder.fromPath("/activity")
                .queryParam("type", type)
                .toUriString();
        return webClient.get()
                .uri(uriString).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Activity.class);
    }

}

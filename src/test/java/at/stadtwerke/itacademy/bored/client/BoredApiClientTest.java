package at.stadtwerke.itacademy.bored.client;

import at.stadtwerke.itacademy.bored.model.Activity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
class BoredApiClientTest {

    private BoredApiClient boredApiClient;
    private MockWebServer mockWebServer;

    @BeforeEach
    public void setUp() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("").toString())
                .build();

        this.boredApiClient = new BoredApiClient(webClient);
    }

    @AfterEach
    public void shutDown() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    public void getSimpleActivity_ApiSendsActivity_ReturnsActivity() {
        this.mockWebServer.enqueue(new MockResponse()
                .setBody("{\"activity\":\"Go fishing\",\"type\":\"recreational\"}")
                .addHeader("Content-Type", "application/json"));

        Mono<Activity> result = boredApiClient.getSimpleActivity();

        StepVerifier.create(result)
                .assertNext(activity -> {
                    Assertions.assertEquals("Go fishing", activity.getActivity());
                    Assertions.assertEquals("recreational", activity.getType());
                })
                .verifyComplete();
    }

    @Test
    public void getSimpleActivity_ApiSendsEmptyString_ReturnsNull() {
        this.mockWebServer.enqueue(new MockResponse()
                .setBody("")
                .addHeader("Content-Type", "application/json"));

        Mono<Activity> result = boredApiClient.getSimpleActivity();

        Assertions.assertNull(result.block());
    }


    @Test
    public void getSimpleActivity_ApiReturns500Error_ThrowsException() {
        this.mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        Mono<Activity> result = boredApiClient.getSimpleActivity();

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof WebClientResponseException &&
                                ((WebClientResponseException) throwable).getStatusCode().value() == 500)
                .verify();
    }


    @Test
    public void getActivityByType_ApiSendsActivity_ReturnsActivity() {
        this.mockWebServer.enqueue(new MockResponse()
                .setBody("{\"activity\":\"Go fishing\",\"type\":\"recreational\",\"price\":5}")
                .addHeader("Content-Type", "application/json"));

        Mono<Activity> result = boredApiClient.getActivityByType("recreational");

        StepVerifier.create(result)
                .assertNext(activity -> {
                    Assertions.assertEquals("Go fishing", activity.getActivity());
                    Assertions.assertEquals("recreational", activity.getType());
                    Assertions.assertEquals(5, activity.getPrice());
                })
                .verifyComplete();
    }


    private static Stream<Arguments> activitiesForTesting() {
        Activity cookingActivity = new Activity();
        cookingActivity.setActivity("Cooking a new dish");
        cookingActivity.setType("cooking");
        cookingActivity.setPrice(4);
        cookingActivity.setParticipants(3);
        cookingActivity.setLink("https://chefkoch.de");
        cookingActivity.setAccessibility(1);

        Activity educationActivity = new Activity();
        educationActivity.setActivity("Learning a new language");
        educationActivity.setType("education");
        educationActivity.setPrice(43);
        educationActivity.setParticipants(100);
        educationActivity.setLink("https://duolingo.com");
        educationActivity.setAccessibility(1);

        return Stream.of(Arguments.of("cooking", cookingActivity),
                         Arguments.of("education", educationActivity));
    }

    @ParameterizedTest
    @MethodSource("activitiesForTesting")
    public void getActivityByType_ReturnsActivity(String type, Activity expectedActivity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(expectedActivity);
        this.mockWebServer.enqueue(new MockResponse()
                .setBody(json)
                .addHeader("Content-Type", "application/json"));

        Mono<Activity> result = boredApiClient.getActivityByType(type);

        StepVerifier.create(result)
                .assertNext(activity -> {
                    Assertions.assertEquals(expectedActivity.getActivity(), activity.getActivity());
                    Assertions.assertEquals(expectedActivity.getType(), activity.getType());
                    Assertions.assertEquals(expectedActivity.getPrice(), activity.getPrice());
                    Assertions.assertEquals(expectedActivity.getParticipants(), activity.getParticipants());
                    Assertions.assertEquals(expectedActivity.getLink(), activity.getLink());
                    Assertions.assertEquals(expectedActivity.getAccessibility(), activity.getAccessibility());
                })
                .verifyComplete();
    }

}

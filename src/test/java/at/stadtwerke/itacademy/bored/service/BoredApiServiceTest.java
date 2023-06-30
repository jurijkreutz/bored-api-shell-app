package at.stadtwerke.itacademy.bored.service;

import at.stadtwerke.itacademy.bored.client.BoredApiClient;
import at.stadtwerke.itacademy.bored.model.Activity;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
public class BoredApiServiceTest {

    private BoredApiClient mockClient;

    private BoredApiService boredApiService;

    @BeforeEach
    public void setUp() {
        this.mockClient = Mockito.mock(BoredApiClient.class);
        this.boredApiService = new BoredApiService(mockClient);
    }

    @Test
    public void getActivity_GetsCorrectActivityFromClient_ReturnsActivityBlock() {
        Activity newActivity = new Activity("Go skiing", "recreational",
                                    1, 50, "http://bergfex.at", 3.5);
        Mockito.when(mockClient.getSimpleActivity()).thenReturn(Mono.just(newActivity));

        String activityBlock = boredApiService.getActivity();

        String expected = """
                /-------------------------\\
                | Activity: Go skiing     |
                | Type: recreational      |
                | Link: http://bergfex.at |
                | Participants: 1         |
                | Accessibility: 3.5      |
                | Price: 50.0             |
                \\-------------------------/
                """;
        Assertions.assertEquals(expected, activityBlock);
    }

    @Test
    public void getActivityByType_ReturnsActivityBlock() {
        Activity newActivity = new Activity("Go skiing", "recreational",
                1, 50, "http://bergfex.at", 3.5);
        Mockito.when(mockClient.getActivityByType("recreational")).thenReturn(Mono.just(newActivity));

        String activityBlock = boredApiService.getActivity("recreational");

        String expected = """
                /-------------------------\\
                | Activity: Go skiing     |
                | Type: recreational      |
                | Link: http://bergfex.at |
                | Participants: 1         |
                | Accessibility: 3.5      |
                | Price: 50.0             |
                \\-------------------------/
                """;
        Assertions.assertEquals(expected, activityBlock);
    }


    @Test
    public void getActivity_WithoutLink_ReturnsActivityBlockWithoutLink() {
        Activity newActivity = new Activity();
        newActivity.setActivity("Go fishing");
        newActivity.setType("recreational");
        newActivity.setParticipants(2);
        newActivity.setPrice(20);
        newActivity.setAccessibility(1.5);
        Mockito.when(mockClient.getSimpleActivity()).thenReturn(Mono.just(newActivity));

        String activityBlock = boredApiService.getActivity();

        String expected = """
                /----------------------\\
                | Activity: Go fishing |
                | Type: recreational   |
                | Participants: 2      |
                | Accessibility: 1.5   |
                | Price: 20.0          |
                \\----------------------/
                """;
        Assertions.assertEquals(expected, activityBlock);
    }

    @Test
    public void getActivityText_GetsCorrectActivityFromClient_ReturnsActivityText() {
        Activity newActivity = new Activity("Go skiing", "recreational",
                1, 50, "http://bergfex.at", 3.5);
        Mockito.when(mockClient.getSimpleActivity()).thenReturn(Mono.just(newActivity));

        String activityText = boredApiService.getActivityText();

        String expected =   "Activity Idea: Go skiing";
        Assertions.assertEquals(expected, activityText);
    }

    @Test
    public void getActivity_ClientThrows500Error_ReturnsErrorText() {
        WebClientResponseException exception = get500WebClientResponseException();
        Mockito.when(mockClient.getSimpleActivity()).thenThrow(exception);

        String result = boredApiService.getActivity();

        Assertions.assertEquals("Web Client Error: Try again later. - 500 INTERNAL_SERVER_ERROR", result);
    }

    @NotNull
    private WebClientResponseException get500WebClientResponseException() {
        int statusCode = 500;
        String statusText = "Internal Server Error";
        return new WebClientResponseException(
                statusText,
                statusCode,
                statusText,
                null,
                null,
                null
        );
    }

    @Test
    public void getActivityByType_ClientThrows500Error_ReturnsErrorText() {
        WebClientResponseException exception = get500WebClientResponseException();
        String type = "recreational";
        Mockito.when(mockClient.getActivityByType(type)).thenThrow(exception);

        String result = boredApiService.getActivity(type);

        Assertions.assertEquals("Web Client Error: Try again later. - 500 INTERNAL_SERVER_ERROR", result);
    }

    @Test
    public void getActivityText_ClientThrows500Error_ReturnsErrorText() {
        WebClientResponseException exception = get500WebClientResponseException();
        Mockito.when(mockClient.getSimpleActivity()).thenThrow(exception);

        String result = boredApiService.getActivityText();

        Assertions.assertEquals("Web Client Error: Try again later. - 500 INTERNAL_SERVER_ERROR", result);
    }

    @Test
    public void getActivity_GetsEmptyActivityString_ReturnsErrorText() {
        Activity newActivity = new Activity();
        Mockito.when(mockClient.getSimpleActivity()).thenReturn(Mono.just(newActivity));

        String activityText = boredApiService.getActivity();

        String expected = "Error: The activity String is empty.";
        Assertions.assertEquals(expected, activityText);
    }

    @Test
    public void getActivityByType_GetsEmptyActivityString_ReturnsErrorText() {
        Activity newActivity = new Activity();
        String type = "recreational";
        Mockito.when(mockClient.getActivityByType(type)).thenReturn(Mono.just(newActivity));

        String activityText = boredApiService.getActivity(type);

        String expected = "Error: The activity String is empty.";
        Assertions.assertEquals(expected, activityText);
    }

    @Test
    public void getActivityText_GetsEmptyActivityString_ReturnsErrorText() {
        Activity newActivity = new Activity();
        Mockito.when(mockClient.getSimpleActivity()).thenReturn(Mono.just(newActivity));

        String activityText = boredApiService.getActivityText();

        String expected = "Error: The activity String is empty.";
        Assertions.assertEquals(expected, activityText);
    }

    @Test
    public void getActivity_GetsEmptyActivity_ReturnsErrorText() {
        Mockito.when(mockClient.getSimpleActivity()).thenReturn(Mono.empty());

        String activityText = boredApiService.getActivity();

        String expected = "Error: BoredApi didn't send an activity.";
        Assertions.assertEquals(expected, activityText);
    }

    @Test
    public void getActivityText_GetsEmptyActivity_ReturnsErrorText() {
        Mockito.when(mockClient.getSimpleActivity()).thenReturn(Mono.empty());

        String activityText = boredApiService.getActivityText();

        String expected = "Error: BoredApi didn't send an activity.";
        Assertions.assertEquals(expected, activityText);
    }

}

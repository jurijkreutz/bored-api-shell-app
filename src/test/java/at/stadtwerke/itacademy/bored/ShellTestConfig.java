package at.stadtwerke.itacademy.bored;

import at.stadtwerke.itacademy.bored.client.BoredApiClient;
import at.stadtwerke.itacademy.bored.service.BoredApiService;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class ShellTestConfig {

    @Bean
    public MockWebServer webServerMock() {
        MockWebServer webServer = new MockWebServer();
        webServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                String type = request.getRequestUrl().queryParameter("type");
                if (type == null) {
                    return new MockResponse()
                            .setBody("{\"activity\":\"Go fishing\",\"type\":\"recreational\"}")
                            .addHeader("Content-Type", "application/json");
                } else if (type.equals("education")) {
                    return new MockResponse()
                            .setBody("{\"activity\":\"Learn a new language\",\"type\":\"education\"}")
                            .addHeader("Content-Type", "application/json");
                } else if (type.equals("social")) {
                    return new MockResponse()
                            .setBody("{\"activity\":\"Meet old friends\",\"type\":\"social\"}")
                            .addHeader("Content-Type", "application/json");
                }
                return new MockResponse().setResponseCode(404);
            }
        });
        return webServer;
    }

    @Bean
    public WebClient webClient(MockWebServer webServerMock) {
        return WebClient.builder()
                .baseUrl(webServerMock.url("").toString())
                .build();
    }

    @Bean
    public BoredApiClient boredApiClient(WebClient webClient) {
        return new BoredApiClient(webClient);
    }

    @Bean
    public BoredApiService boredApiService(BoredApiClient boredApiClient) {
        return new BoredApiService(boredApiClient);
    }


}

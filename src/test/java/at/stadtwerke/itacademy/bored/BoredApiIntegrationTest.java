package at.stadtwerke.itacademy.bored;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.shell.test.ShellAssertions;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.autoconfigure.ShellTest;
import java.util.concurrent.TimeUnit;
import static org.awaitility.Awaitility.await;


@ShellTest
@Import(ShellTestConfig.class)
public class BoredApiIntegrationTest {


    @Autowired
    private ShellTestClient testClient;

    @Test
    public void help_ReturnsAvailableCommands() {
        ShellTestClient.NonInteractiveShellSession session = testClient
                .nonInterative("help")
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("AVAILABLE COMMANDS");
        });
    }

    @Test
    public void getActivity_ReturnsActivityBlock() {
        ShellTestClient.NonInteractiveShellSession session = testClient
                .nonInterative("get-activity")
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("Activity: Go fishing")
                    .containsText("Type: recreational");
        });
    }

    @Test
    public void getSimpleActivity_ReturnsActivityText() {
        ShellTestClient.NonInteractiveShellSession session = testClient
                .nonInterative("get-simple-activity")
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("Activity Idea: Go fishing");
        });
    }

    @Test
    public void getActivityByType_TypesEducation_ReturnsEducationActivity() {
        ShellTestClient.NonInteractiveShellSession session = testClient
                .nonInterative("get-activity-by-type", "--type", "education")
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("Activity: Learn a new language")
                    .containsText("Type: education");
        });
    }

    @Test
    public void getActivityByType_TypesSocial_ReturnsSocialActivity() {
        ShellTestClient.NonInteractiveShellSession session = testClient
                .nonInterative("get-activity-by-type", "--type", "social")
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("Activity: Meet old friends")
                    .containsText("Type: social");
        });
    }

    @Test
    public void getActivityByType_TypesInvalidType_ReturnsErrorMessage() {
        ShellTestClient.NonInteractiveShellSession session = testClient
                .nonInterative("get-activity-by-type", "--type", "invalid")
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("Web Client Error: Try again later. - 404 NOT_FOUND");
        });
    }

    @Test
    public void hello_ReturnsHelloMessage() {
        ShellTestClient.NonInteractiveShellSession session = testClient
                .nonInterative("hello")
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("Welcome to Jurijs Shell Application!");
        });
    }

    @Test
    public void add_ReturnsSum() {
        ShellTestClient.NonInteractiveShellSession session = testClient
                .nonInterative("add", "12", "23")
                .run();

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ShellAssertions.assertThat(session.screen())
                    .containsText("35");
        });
    }

}
package at.stadtwerke.itacademy.bored.commands;

import at.stadtwerke.itacademy.bored.service.BoredApiService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BoredApiCommandsTest {

    private BoredApiService mockService;

    private BoredApiCommands boredApiCommands;

    @BeforeEach
    public void setUp() {
        this.mockService = Mockito.mock(BoredApiService.class);
        this.boredApiCommands = new BoredApiCommands(mockService);
    }

    @Test
    public void getActivity_GetsActivityFromService_ReturnsActivityBlock() {
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
        Mockito.when(mockService.getActivity()).thenReturn(expected);

        String result = boredApiCommands.getActivity();

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void getActivityByType_GetsActivityByTypeFromService_ReturnsActivityBlock() {
        String expected = """
                /-------------------------\\
                | Activity: Golfing       |
                | Type: recreational      |
                | Link: http://orf.at     |
                | Participants: 2         |
                | Accessibility: 1.5      |
                | Price: 20.0             |
                \\-------------------------/
                """;
        Mockito.when(mockService.getActivity("recreational")).thenReturn(expected);

        String result = boredApiCommands.getActivityByType("recreational");

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void getSimpleActivity_GetsActivityFromService_ReturnsActivityText() {
        String expected = "Activity Idea: Go skiing";
        Mockito.when(mockService.getActivityText()).thenReturn(expected);

        String result = boredApiCommands.getSimpleActivity();

        Assertions.assertEquals(expected, result);
    }

}

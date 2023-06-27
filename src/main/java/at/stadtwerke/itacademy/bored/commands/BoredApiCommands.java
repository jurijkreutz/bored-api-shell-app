package at.stadtwerke.itacademy.bored.commands;

import at.stadtwerke.itacademy.bored.client.BoredApiClient;
import at.stadtwerke.itacademy.bored.model.Activity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class BoredApiCommands {

    private final BoredApiClient boredApiClient;

    public BoredApiCommands(BoredApiClient boredApiClient) {
        this.boredApiClient = boredApiClient;
    }

    @ShellMethod("Get simple activity.")
    public String getActivity() {
        Activity activity = boredApiClient.getSimpleActivity().block();
        assert activity != null;
        return activity.getActivity();
    }

}

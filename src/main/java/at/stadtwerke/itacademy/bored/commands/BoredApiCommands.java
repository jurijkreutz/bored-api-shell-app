package at.stadtwerke.itacademy.bored.commands;

import at.stadtwerke.itacademy.bored.service.BoredApiService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class BoredApiCommands {

    private final BoredApiService boredApiService;

    public BoredApiCommands(BoredApiService boredApiService) {
        this.boredApiService = boredApiService;
    }

    @ShellMethod("Get activity block.")
    public String getActivity() {
        return boredApiService.getActivityBlock();
    }

    @ShellMethod("Get simple activity text.")
    public String getSimpleActivity() {
        return boredApiService.getActivityText();
    }

}

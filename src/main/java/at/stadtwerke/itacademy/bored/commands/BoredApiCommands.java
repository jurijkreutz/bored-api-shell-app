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
        return boredApiService.getActivity();
    }

    @ShellMethod("Get activity by type. [\"education\", \"recreational\", \"social\", \"diy\"," +
            "\"charity\", \"cooking\", \"relaxation\", \"music\", \"busywork\"]")
    public String getActivityByType(String type) {
        return boredApiService.getActivity(type);
    }

    @ShellMethod("Get simple activity text.")
    public String getSimpleActivity() {
        return boredApiService.getActivityText();
    }

}

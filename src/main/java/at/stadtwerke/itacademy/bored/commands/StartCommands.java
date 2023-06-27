package at.stadtwerke.itacademy.bored.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class StartCommands {

    @ShellMethod("Greet the user.")
    public String hello() {
        return "Welcome to Jurijs Shell Application!";
    }

    @ShellMethod("Add two integers together.")
    public int add(int a, int b) {
        return a + b;
    }

}

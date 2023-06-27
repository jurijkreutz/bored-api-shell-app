package at.stadtwerke.itacademy.bored;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class MyStartCommands {

    @ShellMethod("Greet the user.")
    public String hello() {
        return "Welcome to Jurijs Shell Application!";
    }

    @ShellMethod("Add two integers together.")
    public int add(int a, int b) {
        return a + b;
    }

}

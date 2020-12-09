package handlers;

public class ConsoleOutputHandler extends OutputHandler {
    private final String output;

    public ConsoleOutputHandler(String output) {
        this.output = output;
    }

    @Override
    public void handle() {
        System.out.println(output);
    }
}

package handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileOutputHandler extends OutputHandler {
    private final String filepath;
    private final String output;

    public FileOutputHandler(String filepath, String output) {
        this.output = output;
        this.filepath = filepath;
    }

    @Override
    public void handle() {
        try {
            PrintWriter writer = new PrintWriter(new File(filepath));
            writer.println(output);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

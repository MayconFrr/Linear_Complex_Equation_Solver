package solver;

import argParser.OptionalArgumentParser;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        String inputPath = OptionalArgumentParser.parseArgument(args, "-in");
        String outputPath = OptionalArgumentParser.parseArgument(args, "-out");

        boolean argumentMissing = false;
        if (inputPath == null) {
            System.out.println("Missing argument -in");
            argumentMissing = true;
        }
        if (outputPath == null) {
            System.out.println("Missing argument -out");
            argumentMissing = true;
        }
        if (argumentMissing){
            return;
        }

        try {
            LinearComplexEquationSystem system = new LinearComplexEquationSystem(inputPath);
            system.solve(outputPath);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

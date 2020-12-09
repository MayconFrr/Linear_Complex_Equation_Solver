package solver;

import handlers.ConsoleOutputHandler;
import handlers.FileOutputHandler;
import handlers.OutputHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static solver.ComplexNumber.one;
import static solver.ComplexNumber.zero;

public class LinearComplexEquationSystem {
    private final List<OutputHandler> handlers = new ArrayList<>();

    private final LinearComplexEquation[] rows;

    private final int variableCount;
    private final int equationCount;

    public LinearComplexEquationSystem(String inputPath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(inputPath));

        int[] dimensions = Arrays.stream(scanner.nextLine().split("\\s+"))
            .mapToInt(Integer::parseInt)
            .toArray();

        variableCount = dimensions[0];
        equationCount = dimensions[1];

        rows = new LinearComplexEquation[equationCount];

        for (int i = 0; i < equationCount; i++) {
            rows[i] = new LinearComplexEquation(
                Arrays.stream(scanner.nextLine().split("\\s+"))
                    .map(ComplexNumber::parseComplex)
                    .toArray(ComplexNumber[]::new)
            );
        }

        scanner.close();
    }

    private static void swapRows(LinearComplexEquation[] rows, int r1, int r2) {
        LinearComplexEquation temp = rows[r1];
        rows[r1] = rows[r2];
        rows[r2] = temp;
    }

    private boolean hasNoSolution() {
        return Arrays.stream(rows)
            .anyMatch(LinearComplexEquation::hasNoSolution);
    }

    private boolean hasInfiniteSolutions() {
        int rank = 0;

        for (int i = 0; i < variableCount && i < equationCount; i++) {
            if (!rows[i].getCoefficient(i).equals(zero)) {
                rank++;
            }
        }

        return (rank < variableCount) && !hasNoSolution();
    }

    private void rowEchelonForm() {
        for (int i = 0; i < variableCount && i < equationCount; i++) {
            if (rows[i].getCoefficient(i).equals(zero)) {
                for (int j = i + 1; j < rows.length; j++) {
                    if (!rows[j].getCoefficient(i).equals(zero)) {
                        swapRows(rows, j, i);
                        System.out.println("R" + (i + 1) + " <-> R" + (j + 1));
                        break;
                    }
                }
            }
            if (rows[i].getCoefficient(i).equals(zero)) {
                for (int j = i + 1; j < variableCount; j++) {
                    if (!rows[i].getCoefficient(j).equals(zero)) {
                        rows[i].swapCoefficients(i, j);
                        break;
                    }
                }
            }
            if (rows[i].getCoefficient(i).equals(zero)) {
                for (int j = i + 1; j < equationCount; j++) {
                    for (int k = i + 1; k < variableCount; k++) {
                        if (!rows[j].getCoefficient(k).equals(zero)) {
                            swapRows(rows, i, j);
                            rows[i].swapCoefficients(i, k);
                            System.out.println("R" + (i + 1) + " <-> R" + (j + 1));

                            break;
                        }
                    }
                }
            }
            if (rows[i].getCoefficient(i).equals(zero)) {
                return;
            }

            // Transforming diagonal to 1
            if (!rows[i].getCoefficient(i).equals(one)) {
                ComplexNumber scalar = rows[i].getCoefficient(i).inverse();
                rows[i] = rows[i].multiply(scalar);
                System.out.println(scalar + " * R" + (i + 1) + " -> R" + (i + 1));

            }

            // Zeroing elements below diagonal
            for (int j = i + 1; j < equationCount; j++) {
                if (!rows[j].getCoefficient(i).equals(zero)) {
                    ComplexNumber scalar = new ComplexNumber(-1, 0)
                            .multiply(rows[j].getCoefficient(i))
                            .divide(rows[i].getCoefficient(i));
                    rows[j] = rows[i].multiply(scalar).add(rows[j]);
                    System.out.println(scalar + " * R" + (i + 1) + " + R" + (j + 1) + " -> R" + (j + 1));
                }
            }
        }
    }

    private void zeroAboveDiagonal() {
        for (int i = variableCount - 1; i > 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (!rows[i].getCoefficient(i).equals(zero) && !rows[j].getCoefficient(i).equals(zero)) {
                    ComplexNumber scalar = new ComplexNumber(-1, 0)
                            .multiply(rows[j].getCoefficient(i))
                            .divide(rows[i].getCoefficient(i));
                    rows[j] = rows[j].add(rows[i].multiply(scalar));
                    System.out.println(scalar + " * R" + (i + 1) + " + R" + (j + 1) + " -> R" + (j + 1));
                }
            }
        }
    }

    public void solve(String outputPath) {
        rowEchelonForm();

        if (hasNoSolution()) {
            handlers.add(new ConsoleOutputHandler("No solutions"));
            handlers.add(new FileOutputHandler(outputPath, "No solutions"));
        } else if (hasInfiniteSolutions()) {
            handlers.add(new ConsoleOutputHandler("Infinitely many solutions"));
            handlers.add(new FileOutputHandler(outputPath, "Infinitely many solutions"));
        }
        if (!handlers.isEmpty()) {
            handlers.forEach(OutputHandler::handle);
            return;
        }

        // Reduced row echelon form
        zeroAboveDiagonal();

        handlers.add(
            new FileOutputHandler(
                outputPath,
                Arrays.stream(rows)
                    .map(equation -> equation.getCoefficient(variableCount))
                    .limit(variableCount)
                    .map(ComplexNumber::toString)
                    .collect(Collectors.joining(System.lineSeparator()))
            )
        );
        handlers.add(
            new ConsoleOutputHandler(
                Arrays.stream(rows)
                    .map(equation -> equation.getCoefficient(variableCount))
                    .limit(variableCount)
                    .map(ComplexNumber::toString)
                    .collect(Collectors.joining(", ", "Solution is: (", ")"))
            )
        );

        handlers.forEach(OutputHandler::handle);
    }
}

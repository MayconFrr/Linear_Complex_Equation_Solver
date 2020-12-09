package solver;

import static solver.ComplexNumber.zero;

import java.util.Arrays;
import java.util.stream.IntStream;

public class LinearComplexEquation {
    private final ComplexNumber[] coefficients;

    public LinearComplexEquation(ComplexNumber... coefficients) {
        this.coefficients = coefficients;
    }

    public ComplexNumber getCoefficient(int i) {
        return coefficients[i];
    }

    public LinearComplexEquation multiply(ComplexNumber z) {
        return new LinearComplexEquation(Arrays.stream(coefficients)
            .map(c -> c.multiply(z))
            .toArray(ComplexNumber[]::new)
        );
    }

    public LinearComplexEquation add(LinearComplexEquation e) {
        if (coefficients.length != e.coefficients.length) {
            throw new IllegalArgumentException("Adding equations of different sizes");
        }

        return new LinearComplexEquation(IntStream.range(0, coefficients.length)
            .mapToObj(i -> coefficients[i].add(e.coefficients[i]))
            .toArray(ComplexNumber[]::new)
        );
    }

    public void swapCoefficients(int column1, int column2) {
        ComplexNumber temp = coefficients[column1];
        coefficients[column1] = coefficients[column2];
        coefficients[column2] = temp;
    }

    public boolean isNull() {
        return Arrays.stream(coefficients)
            .limit(coefficients.length - 1)
            .allMatch(z -> z.equals(zero));
    }

    public boolean hasNoSolution() {
        return this.isNull() && !coefficients[coefficients.length - 1].equals(zero);
    }

    @Override
    public String toString() {
        return Arrays.toString(coefficients);
    }
}

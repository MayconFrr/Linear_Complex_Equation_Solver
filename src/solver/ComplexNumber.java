package solver;

public class ComplexNumber {
    public static final ComplexNumber zero = new ComplexNumber(0, 0);
    public static final ComplexNumber one = new ComplexNumber(1, 0);

    public static ComplexNumber parseComplex(String s) {
        s = s.replaceAll(" ", "");
        ComplexNumber parsed = null;
        if (s.contains("+") || (s.contains("-") && s.lastIndexOf('-') > 0)) {
            String re;
            String im;
            s = s.replaceAll("i", "");
            s = s.replaceAll("I", "");
            if (s.indexOf('+') > 0) {
                re = s.substring(0, s.indexOf('+'));
                im = s.substring(s.indexOf('+') + 1);
                parsed = new ComplexNumber(re.isEmpty() ? 1 : Double.parseDouble(re), im.isEmpty() ? 1 : Double.parseDouble(im));
            } else if (s.lastIndexOf('-') > 0) {
                re = s.substring(0, s.lastIndexOf('-'));
                im = s.substring(s.lastIndexOf('-') + 1);
                parsed = new ComplexNumber(Double.parseDouble(re), -Double.parseDouble(im));
            }
        } else {
            if (s.endsWith("i") || s.endsWith("I")) {
                s = s.replaceAll("i", "");
                s = s.replaceAll("I", "");
                if (s.matches("[+\\-]?[0-9]+")) {
                    parsed = new ComplexNumber(0, Double.parseDouble(s));
                } else if (s.equals("-")) {
                    parsed = new ComplexNumber(0, -1);
                } else {
                    parsed = new ComplexNumber(0, 1);
                }
            } else {
                parsed = new ComplexNumber(Double.parseDouble(s), 0);
            }
        }
        return parsed;
    }

    private final double real;
    private final double imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexNumber add(ComplexNumber z) {
        return new ComplexNumber(this.real + z.real, this.imaginary + z.imaginary);
    }

    public ComplexNumber multiply(ComplexNumber z) {
        double real = this.real * z.real - this.imaginary * z.imaginary;
        double imaginary = this.real * z.imaginary + this.imaginary * z.real;
        return new ComplexNumber(real, imaginary);
    }

    public ComplexNumber conjugate() {
        return new ComplexNumber(this.real, -this.imaginary);
    }

    public ComplexNumber divide(ComplexNumber z) {
        ComplexNumber output = this.multiply(z.conjugate());
        double div = Math.pow(z.mod(), 2);
        return new ComplexNumber(output.real / div, output.imaginary / div);
    }

    public ComplexNumber inverse() {
        return one.divide(this);
    }

    public double mod() {
        return Math.sqrt(Math.pow(this.real, 2) + Math.pow(this.imaginary, 2));
    }

    @Override
    public String toString() {
        String re = this.real + "";
        String im = "";
        if (imaginary > 0) {
            im = "+" + (imaginary == 1 ? "" : imaginary) + "i";
        } else if (imaginary < 0) {
            im = (imaginary == -1 ? "-" : imaginary) + "i";
        }
        return re + im;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComplexNumber that = (ComplexNumber) o;

        if (Double.compare(that.real, real) != 0) return false;
        return Double.compare(that.imaginary, imaginary) == 0;
    }

}
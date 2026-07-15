package Practice.LLD.LLDQuestions.Calculator;

public class Main {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        System.out.println("=== Calculator Demo ===");

        run(calculator, 10, 5, Operation.ADD);
        run(calculator, 10, 5, Operation.SUBTRACT);
        run(calculator, 10, 5, Operation.MULTIPLY);
        run(calculator, 10, 5, Operation.DIVIDE);

        System.out.println();
        System.out.println("--- Edge cases ---");
        run(calculator, 7, 0, Operation.DIVIDE);
        run(calculator, -4, 2, Operation.MULTIPLY);
        run(calculator, 3.5, 1.5, Operation.ADD);
    }

    private static void run(Calculator calculator, double a, double b, Operation operation) {
        try {
            double result = calculator.calculate(a, b, operation);
            System.out.println(a + " " + operation + " " + b + " = " + result);
        } catch (ArithmeticException e) {
            System.out.println(a + " " + operation + " " + b + " -> ERROR: " + e.getMessage());
        }
    }
}

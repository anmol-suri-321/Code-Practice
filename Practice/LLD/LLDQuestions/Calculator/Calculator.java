package Practice.LLD.LLDQuestions.Calculator;

public class Calculator {
    public double calculate(double a, double b, Operation operation) {
        OperationStrategy strategy = OperationStrategyFactory.getStrategy(operation);
        return strategy.execute(a, b);
    }
}

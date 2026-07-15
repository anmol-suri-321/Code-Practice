package Practice.LLD.LLDQuestions.Calculator;

public class SubtractOperationStrategy implements OperationStrategy {
    @Override
    public double execute(double a, double b) {
        return a - b;
    }
}

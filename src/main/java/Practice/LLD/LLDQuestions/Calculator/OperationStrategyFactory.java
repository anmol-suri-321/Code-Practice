package Practice.LLD.LLDQuestions.Calculator;

public class OperationStrategyFactory {
    public static OperationStrategy getStrategy(Operation operation) {
        switch (operation) {
            case ADD:
                return new AddOperationStrategy();
            case SUBTRACT:
                return new SubtractOperationStrategy();
            case MULTIPLY:
                return new MultiplyOperationStrategy();
            case DIVIDE:
                return new DivideOperationStrategy();
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }
}

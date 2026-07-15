package Practice.LLD.LLDQuestions.Ecommerce.repository;

public class AlreadyPresentException extends RuntimeException {
    public AlreadyPresentException(String s) {
        super(s);
    }
}

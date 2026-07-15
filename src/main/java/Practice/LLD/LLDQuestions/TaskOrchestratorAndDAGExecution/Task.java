package Practice.LLD.LLDQuestions.TaskOrchestratorAndDAGExecution;

public class Task {
    String id;
    int retries;
    int timeOutinMillis;
    boolean continueOnFailure;

    public Task(String id, int retries, int timeOutinMillis, boolean continueOnFailure) {
        this.id = id;
        this.retries = retries;
        this.timeOutinMillis = timeOutinMillis;
        this.continueOnFailure = continueOnFailure;
    }

    public String getId() {
        return id;
    }

    public int getRetries() {
        return retries;
    }

    public int getTimeOutinMillis() {
        return timeOutinMillis;
    }

    public boolean isContinueOnFailure() {
        return continueOnFailure;
    }
}

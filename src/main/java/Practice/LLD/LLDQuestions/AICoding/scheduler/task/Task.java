package Practice.LLD.LLDQuestions.AICoding.scheduler.task;

import Practice.LLD.LLDQuestions.AICoding.scheduler.model.JobExecutionResult;

public interface Task {
    JobExecutionResult execute(String jobId);
}

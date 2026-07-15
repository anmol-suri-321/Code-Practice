package Demo.AICoding.scheduler.task;

import Demo.AICoding.scheduler.model.JobExecutionResult;

public interface Task {
    JobExecutionResult execute(String jobId);
}

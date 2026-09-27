package Practice.LLD.LLDQuestions.AICoding.scheduler.model;

import Practice.LLD.LLDQuestions.AICoding.scheduler.schedule.Schedule;
import Practice.LLD.LLDQuestions.AICoding.scheduler.task.Task;

public record Job(String jobId, String description, Schedule schedule, Task task) {}

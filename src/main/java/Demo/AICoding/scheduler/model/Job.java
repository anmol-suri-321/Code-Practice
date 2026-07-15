package Demo.AICoding.scheduler.model;

import Demo.AICoding.scheduler.schedule.Schedule;
import Demo.AICoding.scheduler.task.Task;

public record Job(String jobId, String description, Schedule schedule, Task task) {}

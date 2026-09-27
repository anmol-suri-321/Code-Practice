package Practice.LLD.LLDQuestions.AICoding.scheduler.model;

import java.time.Instant;

public record JobExecutionResult(String jobId, Instant executionTime, JobStatus status, String output) {}

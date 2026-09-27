package Practice.LLD.LLDQuestions.AICoding.scheduler;

import Practice.LLD.LLDQuestions.AICoding.scheduler.model.Job;
import Practice.LLD.LLDQuestions.AICoding.scheduler.model.JobExecutionResult;
import Practice.LLD.LLDQuestions.AICoding.scheduler.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    private final ScheduledExecutorService triggerPool;
    private final ExecutorService jobPool;
    private final JobRepository repository;
    private final Map<String, ScheduledFuture<?>> scheduledFutures;

    public SchedulerService(JobRepository repository) {
        this.repository = repository;
        this.triggerPool = Executors.newScheduledThreadPool(4);
        // CachedThreadPool lets multiple jobs run concurrently when schedules overlap.
        this.jobPool = Executors.newCachedThreadPool();
        this.scheduledFutures = new ConcurrentHashMap<>();
    }

    public void scheduleJob(Job job) {
        Optional<Instant> nextTime = job.schedule().nextExecutionTime(Instant.now());
        if (nextTime.isEmpty()) {
            log.info("Job {} has no future execution time, not scheduling", job.jobId());
            return;
        }

        long delayMillis = Math.max(0, Instant.now().until(nextTime.get(), ChronoUnit.MILLIS));
        ScheduledFuture<?> future = triggerPool.schedule(
                () -> executeJob(job), delayMillis, TimeUnit.MILLISECONDS);

        scheduledFutures.put(job.jobId(), future);
        log.info("Scheduled job {} to fire at {} (in {}ms)", job.jobId(), nextTime.get(), delayMillis);
    }

    public void cancelJob(String jobId) {
        ScheduledFuture<?> future = scheduledFutures.remove(jobId);
        if (future != null) {
            future.cancel(false);
            log.info("Cancelled job {}", jobId);
        }
    }

    public void rescheduleJob(Job job) {
        cancelJob(job.jobId());
        scheduleJob(job);
    }

    public void shutdown() {
        triggerPool.shutdown();
        jobPool.shutdown();
        try {
            if (!triggerPool.awaitTermination(5, TimeUnit.SECONDS)) {
                triggerPool.shutdownNow();
            }
            if (!jobPool.awaitTermination(5, TimeUnit.SECONDS)) {
                jobPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            triggerPool.shutdownNow();
            jobPool.shutdownNow();
        }
    }

    private void executeJob(Job job) {
        // Remove the now-fired future before re-scheduling so there's no stale handle.
        scheduledFutures.remove(job.jobId());

        jobPool.submit(() -> {
            log.info("Executing job [{}]", job.jobId());
            JobExecutionResult result = job.task().execute(job.jobId());
            log.info("Job [{}] completed — status={}, time={}", result.jobId(), result.status(), result.executionTime());
            if (result.output() != null && !result.output().isBlank()) {
                log.info("Job [{}] output: {}", result.jobId(), result.output());
            }
        });

        if (job.schedule().isRecurring()) {
            scheduleJob(job);
        } else {
            repository.removeJob(job.jobId());
        }
    }
}

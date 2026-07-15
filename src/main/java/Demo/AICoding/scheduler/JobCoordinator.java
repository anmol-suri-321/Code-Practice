package Demo.AICoding.scheduler;

import Demo.AICoding.scheduler.model.Job;
import Demo.AICoding.scheduler.parser.JobDefinitionParser;
import Demo.AICoding.scheduler.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Coordinates file-system events with the repository and scheduler.
 * Owns the path→jobId mapping needed to resolve deletes (a deleted file has no content to parse).
 */
public class JobCoordinator {

    private static final Logger log = LoggerFactory.getLogger(JobCoordinator.class);

    private final JobDefinitionParser parser;
    private final JobRepository repository;
    private final SchedulerService schedulerService;
    private final Map<Path, String> pathToJobId = new ConcurrentHashMap<>();

    public JobCoordinator(JobDefinitionParser parser, JobRepository repository, SchedulerService schedulerService) {
        this.parser = parser;
        this.repository = repository;
        this.schedulerService = schedulerService;
    }

    public void loadExistingJobs(Path directory) throws IOException {
        try (var paths = Files.list(directory)) {
            paths.filter(p -> p.toString().endsWith(".json"))
                    .forEach(this::onFileAdded);
        }
        log.info("Finished loading existing jobs from {}", directory);
    }

    public void onFileAdded(Path path) {
        try {
            Job job = parser.parse(path);
            pathToJobId.put(path, job.jobId());
            repository.addJob(job);
            schedulerService.scheduleJob(job);
            log.info("Added job [{}] from {}", job.jobId(), path.getFileName());
        } catch (IOException e) {
            log.error("Failed to load job from {}: {}", path, e.getMessage());
        }
    }

    public void onFileModified(Path path) {
        try {
            Job job = parser.parse(path);
            String previousJobId = pathToJobId.get(path);

            // If the job_id changed inside the file, clean up the old entry first.
            if (previousJobId != null && !previousJobId.equals(job.jobId())) {
                schedulerService.cancelJob(previousJobId);
                repository.removeJob(previousJobId);
            }

            pathToJobId.put(path, job.jobId());
            repository.updateJob(job);
            schedulerService.rescheduleJob(job);
            log.info("Updated job [{}] from {}", job.jobId(), path.getFileName());
        } catch (IOException e) {
            log.error("Failed to update job from {}: {}", path, e.getMessage());
        }
    }

    public void onFileDeleted(Path path) {
        String jobId = pathToJobId.remove(path);
        if (jobId != null) {
            schedulerService.cancelJob(jobId);
            repository.removeJob(jobId);
            log.info("Removed job [{}] (file deleted: {})", jobId, path.getFileName());
        }
    }
}

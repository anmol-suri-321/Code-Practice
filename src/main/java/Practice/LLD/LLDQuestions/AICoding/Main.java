package Practice.LLD.LLDQuestions.AICoding;

import Practice.LLD.LLDQuestions.AICoding.scheduler.JobCoordinator;
import Practice.LLD.LLDQuestions.AICoding.scheduler.SchedulerService;
import Practice.LLD.LLDQuestions.AICoding.scheduler.directory.DirectoryWatcherService;
import Practice.LLD.LLDQuestions.AICoding.scheduler.parser.JobDefinitionParser;
import Practice.LLD.LLDQuestions.AICoding.scheduler.parser.JsonJobDefinitionParser;
import Practice.LLD.LLDQuestions.AICoding.scheduler.repository.JobRepository;
import Practice.LLD.LLDQuestions.AICoding.scheduler.task.TaskFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    // Resolved relative to the working directory (project root when run via mvn exec:java).
    private static final String DEFAULT_JOBS_DIR = "src/main/resources/jobs";

    public static void main(String[] args) throws Exception {
        Path jobsDir = Path.of(DEFAULT_JOBS_DIR);

        if (!Files.exists(jobsDir)) {
            Files.createDirectories(jobsDir);
            log.info("Created jobs directory: {}", jobsDir);
        }

        TaskFactory taskFactory = new TaskFactory();
        JobDefinitionParser parser = new JsonJobDefinitionParser(taskFactory);
        JobRepository repository = new JobRepository();
        SchedulerService schedulerService = new SchedulerService(repository);
        JobCoordinator coordinator = new JobCoordinator(parser, repository, schedulerService);

        coordinator.loadExistingJobs(jobsDir);

        DirectoryWatcherService watcher = new DirectoryWatcherService(
                jobsDir,
                coordinator::onFileAdded,
                coordinator::onFileModified,
                coordinator::onFileDeleted);
        watcher.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down ChronoFlow...");
            watcher.stop();
            schedulerService.shutdown();
        }, "shutdown-hook"));

        log.info("ChronoFlow scheduler started. Watching: {}", jobsDir);
        Thread.currentThread().join();
    }
}

package Demo.AICoding.scheduler.task;

import Demo.AICoding.scheduler.model.JobExecutionResult;
import Demo.AICoding.scheduler.model.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.stream.Collectors;

public class ExecuteCommandTask implements Task {

    private static final Logger log = LoggerFactory.getLogger(ExecuteCommandTask.class);

    private final String command;

    public ExecuteCommandTask(String command) {
        this.command = command;
    }

    @Override
    public JobExecutionResult execute(String jobId) {
        Instant executionTime = Instant.now();
        try {
            Process process = new ProcessBuilder("sh", "-c", command)
                    .redirectErrorStream(true)
                    .start();

            String output;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                output = reader.lines().collect(Collectors.joining("\n"));
            }

            int exitCode = process.waitFor();
            JobStatus status = (exitCode == 0) ? JobStatus.SUCCESS : JobStatus.FAILURE;
            log.debug("Command exited with code {} for job {}", exitCode, jobId);
            return new JobExecutionResult(jobId, executionTime, status, output);
        } catch (Exception e) {
            log.error("Command execution failed for job {}: {}", jobId, e.getMessage());
            return new JobExecutionResult(jobId, executionTime, JobStatus.FAILURE, e.getMessage());
        }
    }

    public String getCommand() {
        return command;
    }
}

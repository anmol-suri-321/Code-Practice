package Demo.AICoding.scheduler.parser;

import Demo.AICoding.scheduler.model.Job;
import Demo.AICoding.scheduler.schedule.CronSchedule;
import Demo.AICoding.scheduler.schedule.OneTimeSchedule;
import Demo.AICoding.scheduler.schedule.Schedule;
import Demo.AICoding.scheduler.task.Task;
import Demo.AICoding.scheduler.task.TaskFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonJobDefinitionParser implements JobDefinitionParser {

    // Matches "key": "value" pairs (handles escaped quotes inside values)
    private static final Pattern STRING_FIELD = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");
    // Matches the nested task block: "task": { ... }
    private static final Pattern TASK_BLOCK   = Pattern.compile("\"task\"\\s*:\\s*\\{([^}]*)\\}", Pattern.DOTALL);

    private final TaskFactory taskFactory;

    public JsonJobDefinitionParser(TaskFactory taskFactory) {
        this.taskFactory = taskFactory;
    }

    @Override
    public Job parse(Path filePath) throws IOException {
        String json = Files.readString(filePath);

        String jobId       = requireField(json, "job_id",      filePath);
        String description = requireField(json, "description", filePath);
        String scheduleStr = requireField(json, "schedule",    filePath);

        Matcher taskMatcher = TASK_BLOCK.matcher(json);
        if (!taskMatcher.find()) {
            throw new IOException("Missing 'task' object in " + filePath);
        }
        Map<String, String> taskFields = extractStringFields(taskMatcher.group(1));
        String taskType = taskFields.get("type");
        if (taskType == null) {
            throw new IOException("Missing 'type' inside task block in " + filePath);
        }

        Schedule schedule = parseSchedule(scheduleStr);
        Task task = taskFactory.create(taskType, taskFields);
        return new Job(jobId, description, schedule, task);
    }

    private String requireField(String json, String key, Path filePath) throws IOException {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");
        Matcher m = p.matcher(json);
        if (!m.find()) {
            throw new IOException("Missing required field '" + key + "' in " + filePath);
        }
        return m.group(1);
    }

    private Map<String, String> extractStringFields(String block) {
        Map<String, String> fields = new HashMap<>();
        Matcher m = STRING_FIELD.matcher(block);
        while (m.find()) {
            fields.put(m.group(1), m.group(2));
        }
        return fields;
    }

    private Schedule parseSchedule(String expression) {
        try {
            return new OneTimeSchedule(Instant.parse(expression));
        } catch (DateTimeParseException e) {
            return new CronSchedule(expression);
        }
    }
}

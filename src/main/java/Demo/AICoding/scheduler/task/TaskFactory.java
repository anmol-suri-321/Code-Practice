package Demo.AICoding.scheduler.task;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TaskFactory {

    private final Map<String, Function<Map<String, String>, Task>> registry = new HashMap<>();

    public TaskFactory() {
        register("execute_command", fields -> new ExecuteCommandTask(fields.get("command")));
    }

    /**
     * Registers a new task type. The creator receives the raw key→value fields from the
     * task JSON object, making it easy to add types (e.g. "http_request") without
     * touching core scheduling logic.
     */
    public void register(String type, Function<Map<String, String>, Task> creator) {
        registry.put(type, creator);
    }

    public Task create(String type, Map<String, String> fields) {
        Function<Map<String, String>, Task> creator = registry.get(type);
        if (creator == null) {
            throw new IllegalArgumentException("Unknown task type: " + type);
        }
        return creator.apply(fields);
    }
}

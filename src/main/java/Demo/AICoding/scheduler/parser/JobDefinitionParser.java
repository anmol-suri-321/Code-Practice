package Demo.AICoding.scheduler.parser;

import Demo.AICoding.scheduler.model.Job;

import java.io.IOException;
import java.nio.file.Path;

public interface JobDefinitionParser {
    Job parse(Path filePath) throws IOException;
}

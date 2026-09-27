package Practice.LLD.LLDQuestions.AICoding.scheduler.parser;

import Practice.LLD.LLDQuestions.AICoding.scheduler.model.Job;

import java.io.IOException;
import java.nio.file.Path;

public interface JobDefinitionParser {
    Job parse(Path filePath) throws IOException;
}

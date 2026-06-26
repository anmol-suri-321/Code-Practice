package Practice.LLD.LLDQuestions.FileProcessingStream.model;

import java.util.List;
import java.util.Map;

public class ProcessingResult {
    private final int recordsProcessed;
    private final List<String> errors;
    private final Map<String, Object> rawData;

    public ProcessingResult(int recordsProcessed, List<String> errors, Map<String, Object> rawData) {
        this.recordsProcessed = recordsProcessed;
        this.errors = errors;
        this.rawData = rawData;
    }

    public int getRecordsProcessed() {
        return recordsProcessed;
    }

    public List<String> getErrors() {
        return errors;
    }

    public Map<String, Object> getRawData() {
        return rawData;
    }
}

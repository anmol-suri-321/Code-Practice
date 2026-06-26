package LLD.LLDQuestions.FileProcessingStream.model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class FileJob {
    private final String id;
    private final String filePath;
    private final FileType fileType;
    private final AtomicInteger retryCount = new AtomicInteger(0);
    private final AtomicReference<FileStatus> fileStatus = new AtomicReference<>(FileStatus.PENDING);
    private final FailureMode failureMode;
    private final int maxRetries;
    private ProcessingResult result;

    public FileJob(String id, String filePath, FileType fileType, FailureMode failureMode, int maxRetries) {
        this.id = id;
        this.filePath = filePath;
        this.fileType = fileType;
        this.failureMode = failureMode;
        this.maxRetries = maxRetries;
    }

    public String getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public FileType getFileType() {
        return fileType;
    }

    public AtomicInteger getRetryCount() {
        return retryCount;
    }

    public FailureMode getFailureMode() {
        return failureMode;
    }

    public AtomicReference<FileStatus> getFileStatus() {
        return fileStatus;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public ProcessingResult getResult() {
        return result;
    }

    public void setResult(ProcessingResult result) {
        this.result = result;
    }

    public boolean compareAndSetStatus(FileStatus expected, FileStatus newStatus) {
        return fileStatus.compareAndSet(expected, newStatus);
    }
}

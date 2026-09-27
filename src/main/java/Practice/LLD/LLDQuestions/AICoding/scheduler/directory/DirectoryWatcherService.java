package Practice.LLD.LLDQuestions.AICoding.scheduler.directory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.function.Consumer;

public class DirectoryWatcherService {

    private static final Logger log = LoggerFactory.getLogger(DirectoryWatcherService.class);

    private final Path watchDirectory;
    private final Consumer<Path> onFileAdded;
    private final Consumer<Path> onFileModified;
    private final Consumer<Path> onFileDeleted;

    private volatile boolean running = false;
    private Thread watchThread;
    private WatchService watchService;

    public DirectoryWatcherService(
            Path watchDirectory,
            Consumer<Path> onFileAdded,
            Consumer<Path> onFileModified,
            Consumer<Path> onFileDeleted) {
        this.watchDirectory = watchDirectory;
        this.onFileAdded = onFileAdded;
        this.onFileModified = onFileModified;
        this.onFileDeleted = onFileDeleted;
    }

    public void start() throws IOException {
        watchService = FileSystems.getDefault().newWatchService();
        watchDirectory.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);

        running = true;
        watchThread = new Thread(this::watchLoop, "directory-watcher");
        watchThread.setDaemon(true);
        watchThread.start();
        log.info("Watching directory for job changes: {}", watchDirectory);
    }

    public void stop() {
        running = false;
        if (watchThread != null) {
            watchThread.interrupt();
        }
        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException ignored) {}
        }
    }

    /** Polls until the file is non-empty (write completed) or 2 seconds have elapsed. */
    private void awaitFileReady(Path file) {
        long deadline = System.currentTimeMillis() + 2_000;
        while (System.currentTimeMillis() < deadline) {
            try {
                if (file.toFile().length() > 0) return;
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void watchLoop() {
        while (running) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (ClosedWatchServiceException e) {
                break;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                @SuppressWarnings("unchecked")
                Path filename = watchDirectory.resolve(((WatchEvent<Path>) event).context());

                if (!filename.toString().endsWith(".json")) {
                    continue;
                }

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    log.info("Detected new job file: {}", filename);
                    // Brief pause so the OS finishes flushing the file content before we read it.
                    // ENTRY_CREATE fires when the directory entry appears, not when the write completes.
                    awaitFileReady(filename);
                    onFileAdded.accept(filename);
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    log.info("Detected modified job file: {}", filename);
                    onFileModified.accept(filename);
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    log.info("Detected deleted job file: {}", filename);
                    onFileDeleted.accept(filename);
                }
            }

            if (!key.reset()) {
                log.warn("Watch key invalidated — directory may have been deleted");
                break;
            }
        }
    }
}

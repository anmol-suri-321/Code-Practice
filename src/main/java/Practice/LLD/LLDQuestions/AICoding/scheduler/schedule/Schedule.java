package Practice.LLD.LLDQuestions.AICoding.scheduler.schedule;

import java.time.Instant;
import java.util.Optional;

public interface Schedule {
    Optional<Instant> nextExecutionTime(Instant from);
    boolean isRecurring();
}

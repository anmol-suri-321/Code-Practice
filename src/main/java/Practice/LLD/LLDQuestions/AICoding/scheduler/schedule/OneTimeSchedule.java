package Practice.LLD.LLDQuestions.AICoding.scheduler.schedule;

import java.time.Instant;
import java.util.Optional;

public class OneTimeSchedule implements Schedule {

    private final Instant scheduledTime;

    public OneTimeSchedule(Instant scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    @Override
    public Optional<Instant> nextExecutionTime(Instant from) {
        // Once `from` has passed the scheduled time, there is no next execution.
        if (from.isAfter(scheduledTime)) {
            return Optional.empty();
        }
        return Optional.of(scheduledTime);
    }

    @Override
    public boolean isRecurring() {
        return false;
    }

    public Instant getScheduledTime() {
        return scheduledTime;
    }
}

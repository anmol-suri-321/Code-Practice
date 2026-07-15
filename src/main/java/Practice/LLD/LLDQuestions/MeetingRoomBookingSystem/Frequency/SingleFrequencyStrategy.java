package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Frequency;

import java.time.LocalDate;
import java.util.Set;

public class SingleFrequencyStrategy implements FrequencyStrategy {
    @Override
    public Set<Integer> getDays(LocalDate start, LocalDate end) {
        return Set.of(start.getDayOfYear());
    }
}

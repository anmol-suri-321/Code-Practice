package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Frequency;

import java.time.LocalDate;
import java.util.Set;

public interface FrequencyStrategy {
    Set<Integer> getDays(LocalDate start, LocalDate end);
}

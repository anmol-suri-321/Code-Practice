package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Frequency;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class MonthlyFrequencyStrategy implements FrequencyStrategy {
    @Override
    public Set<Integer> getDays(LocalDate start, LocalDate end) {
        Set<Integer> days = new HashSet<>();

        for(LocalDate d = start; !d.isAfter(end); d = d.plusMonths(1)) {
            days.add(d.getDayOfYear());
        }
        return days;
    }
}

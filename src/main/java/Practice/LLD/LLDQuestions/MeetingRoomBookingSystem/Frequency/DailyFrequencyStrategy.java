package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Frequency;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class DailyFrequencyStrategy implements FrequencyStrategy {
    @Override
    public Set<Integer> getDays(LocalDate start, LocalDate end) {
        Set<Integer> days = new HashSet<>();
        int startDay = start.getDayOfYear();
        int endDay = end.getDayOfYear();

        for(int d = startDay; d <= endDay; d++) {
            days.add(d);
        }
        return days;
    }
}

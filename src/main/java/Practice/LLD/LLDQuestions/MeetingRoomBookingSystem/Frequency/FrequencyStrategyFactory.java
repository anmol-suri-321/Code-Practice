package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Frequency;

public class FrequencyStrategyFactory {
    public static FrequencyStrategy getStrategy(BookingFrequency frequency) {
        switch (frequency) {
            case SINGLE: return new SingleFrequencyStrategy();
            case DAILY:  return new DailyFrequencyStrategy();
            case WEEKLY: return new WeeklyFrequencyStrategy();
            case MONTHLY: return new MonthlyFrequencyStrategy();
            default: throw new IllegalArgumentException("Unknown frequency: " + frequency);
        }
    }
}

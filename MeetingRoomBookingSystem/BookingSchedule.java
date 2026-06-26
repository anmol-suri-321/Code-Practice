package MeetingRoomBookingSystem;

import java.time.LocalDate;

public class BookingSchedule {
    private LocalDate startDate;
    private LocalDate endDate;

    private BookingFrequency frequency;


    public BookingSchedule(LocalDate startDate, LocalDate endDate, BookingFrequency frequency) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.frequency = frequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BookingFrequency getFrequency() {
        return frequency;
    }
}

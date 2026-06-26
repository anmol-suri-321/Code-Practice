package MeetingRoomBookingSystem;

public class Booking {
    private String bookingId;
    private BookingUser organiser;
    private MeetingRoom room;
    private TimeSlot timeSlot;
    private BookingStatus status;
    private BookingSchedule schedule;

    public Booking(String bookingId, BookingUser organiser, MeetingRoom room, TimeSlot timeSlot, BookingStatus status, BookingSchedule schedule) {
        this.bookingId = bookingId;
        this.organiser = organiser;
        this.room = room;
        this.timeSlot = timeSlot;
        this.status = status;
        this.schedule = schedule;
    }

    public BookingUser getOrganiser() {
        return organiser;
    }

    public MeetingRoom getRoom() {
        return room;
    }

    public String getBookingId() {
        return bookingId;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public BookingSchedule getSchedule() {
        return schedule;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}

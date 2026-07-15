package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem;

public class Booking {
    private String bookingId;
    private BookingUser organiser;
    private MeetingRoom room;
    private TimeSlot timeSlot;
    private BookingStatus status;
    private int bookingCapacity;
    private BookingSchedule schedule;

    public Booking(String bookingId, BookingUser organiser, MeetingRoom room, TimeSlot timeSlot, BookingStatus status, int bookingCapacity, BookingSchedule schedule) {
        this.bookingId = bookingId;
        this.organiser = organiser;
        this.room = room;
        this.timeSlot = timeSlot;
        this.status = status;
        this.bookingCapacity = bookingCapacity;
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

    public int getBookingCapacity() {
        return bookingCapacity;
    }

    public BookingSchedule getSchedule() {
        return schedule;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}

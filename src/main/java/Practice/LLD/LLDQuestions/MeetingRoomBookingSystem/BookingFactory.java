package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem;

public class BookingFactory {
    private BookingFactory() {
        /* This utility class should not be instantiated */
    }

    public static Booking create(BookingRequest request) {
        String bookingId = request.getBookingId();
        BookingUser organiser = request.getOrganiser();
        MeetingRoom room = request.getRoom();
        TimeSlot slot = request.getTimeSlot();
        BookingStatus bookingStatus = request.getStatus();
        int capacity = request.getBookingCapacity();
        BookingSchedule schedule = request.getSchedule();

        return new Booking(bookingId, organiser, room, slot, bookingStatus, capacity, schedule);
    }
}

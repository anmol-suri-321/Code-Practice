package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem;

import java.util.List;

public class BookingService {

    public Booking createbooking(BookingRequest request) {
        Booking booking = BookingFactory.create(request);
        MeetingRoom room = booking.getRoom();
        TimeSlot timeSlot = booking.getTimeSlot();
        BookingSchedule schedule = booking.getSchedule();
        int attendees = booking.getBookingCapacity();

        if(booking.getRoom().isRoomAvailable(attendees, timeSlot, schedule)) {
            room.addbooking(booking);
            return booking;
        }

        return null;
    }

    public void cancelBooking(MeetingRoom room, Booking booking) {
        List<Booking> bookings = room.getBookings();
        if(bookings.contains(booking)) {
            bookings.remove(booking);
            booking.setStatus(BookingStatus.CANCELLED);
        }
    }
}

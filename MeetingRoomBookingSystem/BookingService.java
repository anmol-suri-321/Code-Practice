package MeetingRoomBookingSystem;

import java.util.List;

public class BookingService {

    public Booking createbooking(String bookingId, BookingUser organiser, MeetingRoom room, TimeSlot timeSlot, BookingStatus status, BookingSchedule schedule) {
        Booking booking = new Booking(bookingId, organiser, room, timeSlot, status, schedule);

        if(room.isRoomAvailable(timeSlot, schedule)) {
            room.addbooking(booking);
        } else {
            return null;
        }
        room.addbooking(booking);
        return booking;
    }

    public void cancelBooking(MeetingRoom room, Booking booking) {
        List<Booking> bookings = room.getBookings();
        if(bookings.contains(booking)) {
            bookings.remove(booking);
        }

        booking.setStatus(BookingStatus.CANCELLED);
    }
}

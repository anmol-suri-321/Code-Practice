package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem;

import Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Repository.BookingRepository;
import Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.cache.IdempotencyStore;

public class BookingService {
    private final IdempotencyStore<Booking> bookingIdempotencyStore;
    private final BookingRepository bookingRepository;

    public BookingService(IdempotencyStore<Booking> bookingIdempotencyStore, BookingRepository inMemoryBookingRepository) {
        this.bookingIdempotencyStore = bookingIdempotencyStore;
        this.bookingRepository = inMemoryBookingRepository;
    }

    public Booking createbooking(BookingRequest request) {
        Booking booking = BookingFactory.create(request);
        MeetingRoom room = booking.getRoom();

        if(bookingIdempotencyStore.contains(booking.getBookingId())) {
            System.out.println("Duplicate Booking found with Booking ID: " + booking.getBookingId());
            return bookingIdempotencyStore.get(booking.getBookingId());
        }

        if(room.checkAndBookMeetingRoom(booking)) {
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
            bookingIdempotencyStore.save(booking.getBookingId(), booking);
            return booking;
        }

        // meeting room was already booked
        return null;
    }

    public void cancelBooking(MeetingRoom room, Booking booking) {
        if (booking.getStatus() == BookingStatus.CANCELLED ||
                booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel booking with status: " + booking.getStatus());
        }
        if(room.removeBooking(booking)) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.update(booking);
        }
    }
}

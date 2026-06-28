package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Repository;

import Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Booking;
import Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.BookingUser;
import Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.MeetingRoom;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    void save(Booking booking);
    Optional<Booking> findById(String bookingId);
    void update(Booking booking);
    void delete(String bookingId);
    List<Booking> findByRoom(MeetingRoom room);
    List<Booking> findByUser(BookingUser user);
}

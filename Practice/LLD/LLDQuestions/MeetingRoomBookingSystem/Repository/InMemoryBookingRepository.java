package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Repository;

import Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Booking;
import Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.BookingUser;
import Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.MeetingRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBookingRepository implements BookingRepository {
    private final Map<String, Booking> store = new ConcurrentHashMap<>();

    @Override
    public void save(Booking booking) {
        store.put(booking.getBookingId(), booking);
    }

    @Override
    public Optional<Booking> findById(String bookingId) {
        return Optional.ofNullable(store.get(bookingId));
    }

    @Override
    public void update(Booking booking) {
        store.put(booking.getBookingId(), booking);
    }

    @Override
    public void delete(String bookingId) {
        store.remove(bookingId);
    }

    @Override
    public List<Booking> findByRoom(MeetingRoom room) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : store.values()) {
            if (b.getRoom().getRoomId().equals(room.getRoomId())) {
                result.add(b);
            }
        }
        return result;
    }

    @Override
    public List<Booking> findByUser(BookingUser user) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : store.values()) {
            if (b.getOrganiser().getId().equals(user.getId())) {
                result.add(b);
            }
        }
        return result;
    }
}

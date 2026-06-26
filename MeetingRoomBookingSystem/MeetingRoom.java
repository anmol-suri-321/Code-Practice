package MeetingRoomBookingSystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MeetingRoom {
    private String roomId;
    private String roomName;
    private int capacity;

    private List<Booking> bookings;

    public MeetingRoom(String roomId, String roomName, int capacity) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.bookings = new ArrayList<>();
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void addbooking(Booking booking) {
        bookings.add(booking);
    }

    // expected 12->1, booked 11:30->12:30 → overlap → return false
    public boolean isRoomAvailable(TimeSlot timeSlot, BookingSchedule bookingSchedule) {
        Set<Integer> requestedDays = getDaysOfYear(bookingSchedule);

        for (Booking book : bookings) {
            TimeSlot slot = book.getTimeSlot();
            BookingSchedule schedule = book.getSchedule();

            Set<Integer> bookedDays = getDaysOfYear(schedule);

            boolean daysOverlap = requestedDays.stream().anyMatch(bookedDays::contains);
            boolean timesOverlap = timeSlot.getStartTime().isBefore(slot.getEndTime())
                    && timeSlot.getEndTime().isAfter(slot.getStartTime());

            if (daysOverlap && timesOverlap) {
                return false;
            }
        }
        return true;
    }

    private Set<Integer> getDaysOfYear(BookingSchedule schedule) {
        Set<Integer> days = new HashSet<>();
        LocalDate start = schedule.getStartDate();
        LocalDate end = schedule.getEndDate();

        if (schedule.getFrequency().equals(BookingFrequency.SINGLE)) {
            days.add(start.getDayOfYear());
        } else if (schedule.getFrequency().equals(BookingFrequency.DAILY)) {
            for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                days.add(d.getDayOfYear());
            }
        } else if (schedule.getFrequency().equals(BookingFrequency.WEEKLY)) {
            for (LocalDate d = start; !d.isAfter(end); d = d.plusWeeks(1)) {
                days.add(d.getDayOfYear());
            }
        } else { // MONTHLY
            for (LocalDate d = start; !d.isAfter(end); d = d.plusMonths(1)) {
                days.add(d.getDayOfYear());
            }
        }
        return days;
    }
}

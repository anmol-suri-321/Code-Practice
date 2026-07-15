package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem;

import Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Frequency.FrequencyStrategy;
import Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Frequency.FrequencyStrategyFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MeetingRoom {
    private String roomId;
    private String roomName;
    private int capacity;

    private List<Booking> bookings;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

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

    public boolean isRoomAvailable(int attendees, TimeSlot newTimeSlot, BookingSchedule bookingSchedule) {
        readWriteLock.readLock().lock();
        try {
            return isRoomAvailableCheck(attendees, newTimeSlot, bookingSchedule);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    private boolean isRoomAvailableCheck(int attendees, TimeSlot newTimeSlot, BookingSchedule bookingSchedule) {
        if(attendees > capacity) {
            return false;
        }

        FrequencyStrategy newfrequencyStrategy = FrequencyStrategyFactory.getStrategy(bookingSchedule.getFrequency());
        Set<Integer> requestedDays = newfrequencyStrategy.getDays(bookingSchedule.getStartDate(), bookingSchedule.getEndDate());

        for (Booking book : bookings) {
            TimeSlot bookedSlot = book.getTimeSlot();
            BookingSchedule schedule = book.getSchedule();

            FrequencyStrategy bookedFrequencyStrategy = FrequencyStrategyFactory.getStrategy(schedule.getFrequency());
            Set<Integer> bookedDays = bookedFrequencyStrategy.getDays(schedule.getStartDate(), schedule.getEndDate());

            boolean daysOverlap = checkDaysOverlap(requestedDays, bookedDays);
            boolean timesOverlap = checkTimeOverlap(bookedSlot, newTimeSlot);
            if (daysOverlap && timesOverlap) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkDaysOverlap(Set<Integer> requestedDays, Set<Integer> bookedDays) {
        for(int day : requestedDays) {
            if(bookedDays.contains(day)) {
                return true;
            }
        }

        return false;
    }

    /**
     * booked : 12:00 - 13:00, newSlot : 11:30 - 12;15
     * booked : 12:00 - 13:00, newSlot : 12:50 - 13:10
     * booked : 12:00 - 13:00, newSlot : 11:00 - 14:00
     **/
    private static boolean checkTimeOverlap(TimeSlot bookedSlot, TimeSlot newTimeSlot) {
        return newTimeSlot.getStartTime().isBefore(bookedSlot.getEndTime())
                && newTimeSlot.getEndTime().isAfter(bookedSlot.getStartTime());
    }

    public boolean checkAndBookMeetingRoom(Booking booking) {
        readWriteLock.writeLock().lock();
        try {
            if(isRoomAvailableCheck(booking.getBookingCapacity(), booking.getTimeSlot(), booking.getSchedule())) {
                bookings.add(booking);
                return true;
            }
            return false;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public boolean removeBooking(Booking booking) {
        readWriteLock.writeLock().lock();
        try {
            return bookings.remove(booking);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}

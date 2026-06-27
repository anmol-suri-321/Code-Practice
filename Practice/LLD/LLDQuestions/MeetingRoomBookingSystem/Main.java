package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem;

import Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Frequency.BookingFrequency;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        BookingService service = new BookingService();

        BookingUser organiser = new BookingUser("Anmol", "anmol@gmail.com", "user-1");
        MeetingRoom room = new MeetingRoom("room-1", "Maurya", 10);

        // slot: 12:00 - 13:00, single day booking
        TimeSlot slot1 = new TimeSlot(
                LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0))
        );
        BookingSchedule schedule1 = new BookingSchedule(LocalDate.now(), LocalDate.now(), BookingFrequency.SINGLE);
        BookingRequest request1 = new BookingRequest("booking-1", organiser, room, slot1, BookingStatus.CONFIRMED, 5, schedule1);

        Booking booking1 = service.createbooking(request1);
        System.out.println("Booking 1: " + (booking1 != null ? "CONFIRMED - " + booking1.getBookingId() : "FAILED"));

        // slot: 12:30 - 13:30 — overlaps with booking1, should fail
        TimeSlot slot2 = new TimeSlot(
                LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 30)),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 30))
        );
        BookingSchedule schedule2 = new BookingSchedule(LocalDate.now(), LocalDate.now(), BookingFrequency.SINGLE);
        BookingRequest request2 = new BookingRequest("booking-2", organiser, room, slot2, BookingStatus.CONFIRMED, 5, schedule2);

        Booking booking2 = service.createbooking(request2);
        System.out.println("Booking 2 (overlap): " + (booking2 != null ? "CONFIRMED - " + booking2.getBookingId() : "REJECTED - slot taken"));

        // slot: 14:00 - 15:00 — no overlap, should succeed
        TimeSlot slot3 = new TimeSlot(
                LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0))
        );
        BookingSchedule schedule3 = new BookingSchedule(LocalDate.now(), LocalDate.now(), BookingFrequency.SINGLE);
        BookingRequest request3 = new BookingRequest("booking-3", organiser, room, slot3, BookingStatus.CONFIRMED, 5, schedule3);

        Booking booking3 = service.createbooking(request3);
        System.out.println("Booking 3 (no overlap): " + (booking3 != null ? "CONFIRMED - " + booking3.getBookingId() : "FAILED"));

        // cancel booking1
        service.cancelBooking(room, booking1);
        System.out.println("Booking 1 status after cancel: " + booking1.getStatus());

        // capacity exceeded — room holds 10, request 15
        BookingRequest request4 = new BookingRequest("booking-4", organiser, room, slot3, BookingStatus.CONFIRMED, 15, schedule3);
        Booking booking4 = service.createbooking(request4);
        System.out.println("Booking 4 (over capacity): " + (booking4 != null ? "CONFIRMED" : "REJECTED - over capacity"));
    }
}

package MeetingRoomBookingSystem;

public class BookingUser {
    private String id;
    private String name;
    private String email;


    public BookingUser(String name, String email, String id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

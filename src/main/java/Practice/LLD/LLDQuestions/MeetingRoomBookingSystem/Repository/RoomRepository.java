package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Repository;

import Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.MeetingRoom;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    void save(MeetingRoom room);
    Optional<MeetingRoom> findById(String roomId);
    List<MeetingRoom> findAll();
    void delete(String roomId);
}

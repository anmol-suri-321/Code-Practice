package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.Repository;

import Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.MeetingRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRoomRepository implements RoomRepository {
    private final Map<String, MeetingRoom> dbMap = new ConcurrentHashMap<>();

    @Override
    public void save(MeetingRoom room) {
        dbMap.put(room.getRoomId(), room);
    }

    @Override
    public Optional<MeetingRoom> findById(String roomId) {
        return Optional.ofNullable(dbMap.get(roomId));
    }

    @Override
    public List<MeetingRoom> findAll() {
        return new ArrayList<>(dbMap.values());
    }

    @Override
    public void delete(String roomId) {
        dbMap.remove(roomId);
    }
}

package Practice.LLD.LLDQuestions.MeetingRoomBookingSystem.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IdempotencyStore<T> {
    private Map<String, T> store = new ConcurrentHashMap<>();

    public boolean contains(String key) {
        return store.containsKey(key);
    }

    public void save(String key, T value) {
        store.put(key, value);
    }

    public T get(String key) {
        return store.get(key);
    }
}

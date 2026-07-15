# Meeting Room Booking System — LLD

## 1. Requirements
- Users can book a meeting room for a specific time slot
- Bookings can be one-time (SINGLE) or recurring (DAILY, WEEKLY, MONTHLY)
- Room has a fixed capacity — bookings exceeding capacity are rejected
- Overlapping bookings for the same room are not allowed
- Users can cancel an existing booking
- System must handle concurrent booking requests safely

---

## 2. Entities & Schema

### MeetingRoom
| Field | Type | Notes |
|---|---|---|
| roomId | String | Primary Key |
| roomName | String | |
| capacity | int | Max attendees allowed |
| bookings | List\<Booking\> | In-memory runtime state |

### Booking
| Field | Type | Notes |
|---|---|---|
| bookingId | String | Primary Key, Idempotency Key |
| organiser | BookingUser | Owner of the booking |
| room | MeetingRoom | FK → MeetingRoom |
| timeSlot | TimeSlot | startTime, endTime |
| schedule | BookingSchedule | startDate, endDate, frequency |
| status | BookingStatus | PENDING → CONFIRMED → COMPLETED/CANCELLED |
| bookingCapacity | int | Number of attendees |

### BookingUser
| Field | Type |
|---|---|
| userId | String |
| name | String |
| email | String |

### BookingSchedule
| Field | Type |
|---|---|
| startDate | LocalDate |
| endDate | LocalDate |
| frequency | BookingFrequency (SINGLE/DAILY/WEEKLY/MONTHLY) |

### Relationships
- One `MeetingRoom` → Many `Bookings`
- One `BookingUser` → Many `Bookings`
- One `Booking` → One `TimeSlot` + One `BookingSchedule`

---

## 3. Status Flow

```
PENDING → CONFIRMED → COMPLETED
                    ↘ CANCELLED
```

- `PENDING` — booking request in-flight
- `CONFIRMED` — room successfully locked
- `CANCELLED` — explicitly cancelled by user
- `COMPLETED` — booking time has passed

**Transition Validation:**
- Cannot cancel a `CANCELLED` or `COMPLETED` booking → throws `IllegalStateException`

---

## 4. Design Patterns Used

| Pattern | Where | Why |
|---|---|---|
| Factory | `BookingFactory`, `FrequencyStrategyFactory` | Encapsulate object creation |
| Strategy | `FrequencyStrategy` (Daily/Weekly/Monthly/Single) | Pluggable recurrence logic |
| Repository | `BookingRepository`, `RoomRepository` | Decouple persistence from business logic |

---

## 5. Concurrency Design

### Problem
`isRoomAvailable` + `addBooking` are two separate operations — a race condition allows double booking if two threads pass the availability check simultaneously.

### Solution — `ReentrantReadWriteLock` per `MeetingRoom`
- **Read Lock** — `isRoomAvailable()` — multiple threads can check availability simultaneously
- **Write Lock** — `checkAndBookMeetingRoom()`, `removeBooking()` — exclusive, atomic check-then-write

```
Multiple threads: isRoomAvailable?        → read lock  → concurrent ✅
Single thread:    checkAndBookMeetingRoom → write lock → exclusive  ✅
```

### Key Rules
- `checkAndBookMeetingRoom` calls private `isRoomAvailableCheck()` (no lock) — NOT the public `isRoomAvailable()` — avoids write→read deadlock
- `readWriteLock` is `final` — prevents accidental reassignment
- `getBookings()` returns `Collections.unmodifiableList` — prevents external mutation bypassing the lock

### Why `ReentrantReadWriteLock` over `synchronized`
- `synchronized` blocks all threads including readers
- `ReadWriteLock` allows concurrent reads — better for read-heavy availability checks
- "Reentrant" = same thread can re-acquire the lock without deadlocking itself

---

## 6. Idempotency

### Problem
Client retries on network timeout → same `bookingId` submitted twice → duplicate booking created.

### Solution — `IdempotencyStore<T>`
- Backed by `ConcurrentHashMap` (in-memory) or Redis (production)
- On `createBooking`: check store first → if exists, return stored booking
- On success: save `bookingId → Booking` to store
- On cancel: remove from store

### Race Condition in Idempotency Store
`contains()` + `save()` are not atomic together. Use `putIfAbsent()` for true atomicity:
```java
T existing = store.putIfAbsent(key, value);
if (existing != null) return existing; // duplicate
```

### Production: Redis
- `SET key value NX EX 86400` — atomic set-if-not-exists with 24hr TTL
- TTL prevents unbounded key accumulation
- Shared across all service instances (distributed)
- After TTL expires, same bookingId is treated as a new request

---

## 7. Repository Layer

### Interfaces
- `BookingRepository` — `save`, `findById`, `update`, `delete`, `findByRoom`, `findByUser`
- `RoomRepository` — `save`, `findById`, `findAll`, `delete`

### Implementations
- `InMemoryBookingRepository` — `ConcurrentHashMap<String, Booking>`
- `InMemoryRoomRepository` — `ConcurrentHashMap<String, MeetingRoom>`

### In Production
- Replace with DB-backed implementations (Postgres/MySQL)
- Add `UNIQUE` constraint on `bookingId` column — DB-level duplicate prevention
- Index on `(roomId, startTime, endTime)` for fast availability queries

---

## 8. API Design

### POST /bookings — Create Booking
```
Headers:  X-Idempotency-Key: <bookingId>
Request:  { organiserId, roomId, startTime, endTime, attendees, frequency, scheduleStart, scheduleEnd }
Response:
  201 CREATED     → { bookingId, status: CONFIRMED, ... }
  200 OK          → duplicate request, returns existing booking
  409 CONFLICT    → room not available for requested slot
  400 BAD REQUEST → attendees exceed capacity / invalid time range
```

### DELETE /bookings/{bookingId} — Cancel Booking
```
Response:
  200 OK        → { bookingId, status: CANCELLED }
  404 NOT FOUND → booking not found
  409 CONFLICT  → cannot cancel COMPLETED or already CANCELLED booking
```

### GET /rooms/{roomId}/availability — Check Availability
```
Query:    ?date=2026-06-30&startTime=12:00&endTime=13:00
Response:
  200 OK → { roomId, available: true/false }
  404    → room not found
```

### GET /bookings — Get Bookings by User
```
Query:    ?userId=user-1&page=0&size=10
Response:
  200 OK → [ { bookingId, status, roomId, timeSlot, ... } ]
```

---

## 9. Failure & Recovery

### Failure Scenarios

| Scenario | Impact | Fix |
|---|---|---|
| Crash after room lock, before DB save | Booking lost on restart | Hydrate in-memory state from DB on startup |
| DB save succeeds, idempotency save fails | Retry creates 409 conflict | On idempotency miss, fallback check DB before processing |
| Two threads book same slot | Double booking | Write lock in `checkAndBookMeetingRoom` — atomic ✅ |
| Cancel already cancelled booking | Corrupt state | Status transition validation ✅ |

### Startup Hydration
```java
public void init() {
    List<Booking> confirmed = bookingRepository.findByStatus(BookingStatus.CONFIRMED);
    for (Booking b : confirmed) {
        b.getRoom().checkAndBookMeetingRoom(b); // reload into in-memory room state
    }
}
```

### Idempotency Store Repair
```java
// If idempotency store misses but DB has the booking
Optional<Booking> existing = bookingRepository.findById(request.getBookingId());
if (existing.isPresent()) {
    idempotencyStore.save(request.getBookingId(), existing.get()); // repair cache
    return existing.get();
}
```

---

## 10. Edge Cases

| Case | Handled By |
|---|---|
| Attendees > room capacity | `isRoomAvailableCheck` capacity check |
| Overlapping time slots | `checkTimeOverlap` in `isRoomAvailableCheck` |
| Overlapping recurring bookings | `FrequencyStrategy.getDays` + `checkDaysOverlap` |
| Duplicate booking request (retry) | `IdempotencyStore` |
| Concurrent booking same slot | `ReentrantReadWriteLock` write lock |
| Cancel non-existent booking | `removeBooking` returns false → no-op |
| Cancel COMPLETED/CANCELLED booking | `IllegalStateException` in `cancelBooking` |

---

## 11. Scaling Considerations

| Concern | Approach |
|---|---|
| Read-heavy availability checks | Redis cache (`room:bookings:{roomId}`, 5min TTL), invalidate on write |
| Multiple service instances | Move idempotency store + room lock to Redis (distributed lock via `SET NX`) |
| High write throughput | Partition rooms across nodes — lock scope is per-room already |
| Observability | Log bookingId, roomId, userId, status transitions for every operation |

---

## 12. UML Class Diagram

```
┌─────────────────────┐         ┌──────────────────────────────┐
│    BookingService    │         │         MeetingRoom           │
│─────────────────────│         │──────────────────────────────│
│ - idempotencyStore  │         │ - roomId: String              │
│ - bookingRepository │         │ - roomName: String            │
│─────────────────────│         │ - capacity: int               │
│ + createBooking()   │────────▶│ - bookings: List<Booking>     │
│ + cancelBooking()   │         │ - readWriteLock (final)       │
│ + init()            │         │──────────────────────────────│
└─────────────────────┘         │ + isRoomAvailable()           │
                                │ + checkAndBookMeetingRoom()   │
                                │ + removeBooking()             │
                                │ - isRoomAvailableCheck()      │
                                └──────────────┬───────────────┘
                                               │ has many
                                               ▼
┌─────────────────────┐         ┌──────────────────────────────┐
│    BookingRequest   │         │           Booking             │
│─────────────────────│         │──────────────────────────────│
│ - bookingId         │────────▶│ - bookingId: String           │
│ - organiser         │ creates │ - organiser: BookingUser      │
│ - room              │         │ - room: MeetingRoom           │
│ - timeSlot          │         │ - timeSlot: TimeSlot          │
│ - schedule          │         │ - schedule: BookingSchedule   │
│ - bookingCapacity   │         │ - status: BookingStatus       │
│ - status            │         │ - bookingCapacity: int        │
└─────────────────────┘         │──────────────────────────────│
                                │ + getStatus()                 │
                                │ + setStatus()                 │
                                └──────────────────────────────┘

┌─────────────────────┐         ┌──────────────────────────────┐
│      TimeSlot       │         │       BookingSchedule         │
│─────────────────────│         │──────────────────────────────│
│ - startTime:        │         │ - startDate: LocalDate        │
│   LocalDateTime     │         │ - endDate: LocalDate          │
│ - endTime:          │         │ - frequency: BookingFrequency │
│   LocalDateTime     │         └──────────────┬───────────────┘
└─────────────────────┘                        │ uses
                                               ▼
                                ┌──────────────────────────────┐
                                │      <<interface>>            │
                                │      FrequencyStrategy        │
                                │──────────────────────────────│
                                │ + getDays(): Set<Integer>     │
                                └──────────────┬───────────────┘
                                               │ implemented by
                          ┌────────────────────┼────────────────────┐
                          ▼                    ▼                     ▼
               ┌─────────────────┐  ┌─────────────────┐  ┌──────────────────┐
               │ SingleFrequency │  │ DailyFrequency  │  │ WeeklyFrequency  │
               └─────────────────┘  └─────────────────┘  └──────────────────┘

┌──────────────────────┐         ┌──────────────────────────────┐
│    <<interface>>     │         │        <<interface>>          │
│  BookingRepository   │         │       RoomRepository          │
│──────────────────────│         │──────────────────────────────│
│ + save()             │         │ + save()                      │
│ + findById()         │         │ + findById()                  │
│ + update()           │         │ + findAll()                   │
│ + delete()           │         │ + delete()                    │
│ + findByRoom()       │         └──────────────┬───────────────┘
│ + findByUser()       │                        │ implemented by
└──────────┬───────────┘         ┌──────────────────────────────┐
           │ implemented by      │    InMemoryRoomRepository     │
┌──────────────────────┐         │ - store: ConcurrentHashMap    │
│ InMemoryBookingRepo  │         └──────────────────────────────┘
│ - store:             │
│   ConcurrentHashMap  │
└──────────────────────┘

┌──────────────────────┐
│  IdempotencyStore<T> │
│──────────────────────│
│ - store:             │
│   ConcurrentHashMap  │
│──────────────────────│
│ + contains()         │
│ + save()             │
│ + get()              │
│ + putIfAbsent()      │
│ + remove()           │
└──────────────────────┘

<<enum>> BookingStatus          <<enum>> BookingFrequency
──────────────────────          ─────────────────────────
PENDING                         SINGLE
CONFIRMED                       DAILY
COMPLETED                       WEEKLY
CANCELLED                       MONTHLY
```

---

## 13. DB Indexing

### `bookings` table

| Index | Columns | Type | Why |
|---|---|---|---|
| PK | `booking_id` | Primary | Unique lookup, idempotency check |
| IDX_1 | `room_id` | B-Tree | `findByRoom` — most frequent query |
| IDX_2 | `user_id` | B-Tree | `findByUser` — user's booking history |
| IDX_3 | `room_id, start_time, end_time` | Composite | Overlap check — filter by room + time range |
| IDX_4 | `status` | B-Tree | Startup hydration query (`findByStatus(CONFIRMED)`) |

### `rooms` table

| Index | Columns | Type | Why |
|---|---|---|---|
| PK | `room_id` | Primary | Direct room lookup |
| IDX_1 | `capacity` | B-Tree | Query rooms by minimum capacity |

### Key Indexing Principles
- **Composite index `(room_id, start_time, end_time)`** — overlap check queries `WHERE room_id = ? AND start_time < ? AND end_time > ?`. Room filter first (high cardinality reduction), then time range
- **`status` index** — critical for startup hydration to avoid full table scan
- Avoid over-indexing — every index slows down writes (`save`, `update`). Only index what's queried
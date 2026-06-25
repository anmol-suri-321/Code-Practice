# Jira Ticket System — LLD

---

## Step 1 — Requirements

### Functional Requirements
- Create tickets of different types (Bug, Story)
- Assign tickets to users
- Update ticket status (OPEN → IN_PROGRESS → CLOSED)
- Add comments to tickets
- Notify assignee via Email and WhatsApp on ticket events
- Prevent duplicate operations on retry (idempotency)
- Handle concurrent modifications safely

### Non-Functional Requirements
- Thread-safe ticket mutations
- Pluggable notification channels (add Slack without changing existing code)
- Swappable storage layer (in-memory today, DB tomorrow)
- Extensible ticket types (add Epic without changing factory logic)

---

## Step 2 — Entities / Models

| Entity | Key Fields | Purpose |
|---|---|---|
| `Ticket` (abstract) | id, name, description, status, priority, user, comments, version | Base ticket — never instantiated directly |
| `BugTicket` | + bugSeverity (CRITICAL/MAJOR/MINOR) | Represents a bug |
| `StoryTicket` | + tshirtSize (XS/S/M/L/XL) | Represents a user story |
| `User` | id, name, email, phoneNumber, assignedTickets | Person who creates/gets assigned tickets |
| `Comment` | id, message, timestamp, author | Comment on a ticket |
| `Project` | id, name, description, tickets | Container for tickets |
| `NotificationEvent` | eventType, ticket, user, message | Data carrier for notifications |

### Enums
| Enum | Values |
|---|---|
| `TicketStatus` | OPEN, IN_PROGRESS, CLOSED |
| `TicketType` | BUG, STORY |
| `Priority` | LOW, MEDIUM, HIGH |
| `BugSeverity` | CRITICAL, MAJOR, MINOR |
| `TshirtSize` | XS, S, M, L, XL |
| `NotificationEventType` | TICKET_ASSIGNED, STATUS_CHANGED, COMMENT_ADDED |

---

## Step 3 — Schema & Relationships

```
Project (1) ──────────────── (N) Ticket
                                    │
                          ┌─────────┴──────────┐
                          │                    │
                       BugTicket           StoryTicket
                      + severity           + tshirtSize

Ticket (N) ──── assigned to ──── (1) User
Ticket (1) ──────────────────── (N) Comment
Comment (N) ──── written by ──── (1) User

TicketService ──── uses ──── NotificationService
TicketService ──── uses ──── TicketRepository
TicketService ──── uses ──── IdempotencyStore<Ticket>

NotificationService ──── holds ──── List<NotificationSender>
NotificationSender (interface)
  ├── EmailNotificationSender
  └── WhatsappNotificationSender

TicketRepository (interface)
  └── InMemoryTicketRepository
```

---

## Step 4 — Package Structure

```
JiraTicketSystem/
├── Ticket/
│   ├── Ticket.java                    ← abstract base
│   ├── BugTicket.java
│   ├── StoryTicket.java
│   ├── TicketType.java
│   ├── TicketStatus.java
│   ├── Priority.java
│   ├── BugSeverity.java
│   ├── TshirtSize.java
│   ├── TicketRequest.java             ← internal factory input
│   ├── TicketFactory.java             ← Factory Pattern
│   └── TicketService.java
├── Comment/
│   ├── Comment.java
│   └── CommentService.java
├── Notifications/
│   ├── NotificationSender.java        ← interface (Observer)
│   ├── NotificationService.java
│   ├── NotificationEvent.java
│   ├── NotificationEventType.java
│   ├── EmailNotificationSender.java
│   └── WhatsappNotificationSender.java
├── repository/
│   ├── TicketRepository.java          ← interface
│   └── InMemoryTicketRepository.java
├── cache/
│   └── IdempotencyStore.java          ← generic cache
├── User.java
├── Project.java
└── Main.java
```

---

## Step 5 — UML Class Diagram

```
┌──────────────────────────────────────┐
│          Ticket (abstract)           │
│──────────────────────────────────────│
│ - id: String                         │
│ - name: String                       │
│ - description: String                │
│ - status: TicketStatus               │
│ - priority: Priority                 │
│ - user: User                         │
│ - comments: List<Comment>            │
│ - version: AtomicInteger             │
│──────────────────────────────────────│
│ + assignTicket(user): void           │
│ + addComment(comment): void          │
│ + compareAndIncrementVersion(): bool │
│ + getTicketType(): TicketType        │ ← abstract
└──────────────────────────────────────┘
           △               △
           │               │
┌──────────────┐   ┌───────────────┐
│  BugTicket   │   │  StoryTicket  │
│──────────────│   │───────────────│
│ + severity   │   │ + tshirtSize  │
└──────────────┘   └───────────────┘

TicketRequest ──────► TicketFactory ──────► Ticket subclass

┌─────────────────────────────────────────────┐
│                 TicketService               │
│─────────────────────────────────────────────│
│ - notificationService: NotificationService  │
│ - ticketRepository: TicketRepository        │
│ - idempotencyStore: IdempotencyStore<Ticket>│
│─────────────────────────────────────────────│
│ + createTicket(request, key): Ticket        │
│ + assignTicketToUser(ticket, user, key)     │
│ + updateTicketStatus(ticket, status)        │
│ + findById(id): Optional<Ticket>            │
│ + getTicketsForUser(user): List<Ticket>     │
└─────────────────────────────────────────────┘

NotificationSender (interface)
  + send(event: NotificationEvent): void
    △              △
    │              │
EmailNotifi-   WhatsappNotifi-
cationSender   cationSender

TicketRepository (interface)
  + save / findById / update / delete / findByUser
    △
    │
InMemoryTicketRepository (ConcurrentHashMap)

IdempotencyStore<T>
  + contains(key): boolean
  + save(key, value): void
  + get(key): T
```

---

## Step 6 — Design Patterns Used

### Factory Pattern
`TicketFactory.createTicket(TicketRequest)` — single entry point, caller never instantiates `BugTicket`/`StoryTicket` directly. Adding `EpicTicket` = add case to factory, zero other changes.

### Observer Pattern
| Concept | Implementation |
|---|---|
| Subject | `TicketService`, `CommentService` |
| Observer interface | `NotificationSender` |
| Concrete observers | `EmailNotificationSender`, `WhatsappNotificationSender` |
| Event data | `NotificationEvent` |

### Repository Pattern
`TicketRepository` interface hides storage details. Swap `InMemoryTicketRepository` for `MySQLTicketRepository` — `TicketService` unchanged.

### Dependency Injection
`TicketService` receives `NotificationService`, `TicketRepository`, `IdempotencyStore` via constructor — never creates them internally. `Main.java` controls wiring.

---

## Step 7 — SOLID Principles

| Principle | Application |
|---|---|
| **S** Single Responsibility | `TicketService` manages business rules, `TicketRepository` manages storage, `NotificationService` manages delivery |
| **O** Open/Closed | Add `SlackNotificationSender` — implement interface, register it. Zero changes to existing code |
| **L** Liskov Substitution | `BugTicket`/`StoryTicket` interchangeable as `Ticket`. `Email`/`Whatsapp` interchangeable as `NotificationSender` |
| **I** Interface Segregation | `NotificationSender` has one method. `TicketRepository` has focused CRUD methods only |
| **D** Dependency Inversion | `TicketService` depends on `TicketRepository` interface, not `InMemoryTicketRepository` concretion |

---

## Step 8 — Concurrency

### Problem
Two threads assign same ticket simultaneously — last write wins silently.

### Solution — Optimistic Locking
```
Ticket holds: AtomicInteger version = 0

Before mutation:
1. Read currentVersion
2. compareAndSet(currentVersion, currentVersion + 1)
   → true  = this thread won, proceed
   → false = conflict, throw RuntimeException
3. Mutate ticket
4. Persist to repository
5. Fire notification
```

### Why AtomicInteger not int?
`int++` is not atomic — two threads read version=3, both increment to 4, no conflict detected. `AtomicInteger.compareAndSet` is atomic hardware instruction — exactly one thread wins.

### Why validate before claiming version?
If business rule validation throws after version already claimed — version incremented but ticket unchanged. Inconsistent state. Always: validate → claim version → mutate.

| Scenario | Approach |
|---|---|
| Low contention | Optimistic locking (this system) |
| High contention | Pessimistic locking (DB row lock) |
| Multiple servers | Optimistic or Pessimistic (not synchronized) |

---

## Step 9 — Idempotency

### Problem
Client calls `POST /tickets`, network drops, retries — two identical tickets created.

### Solution
Caller sends unique `idempotencyKey`. Server checks before executing. If seen before, return cached result.

```
First call  (key=abc) → not in store → execute → save(key, result) → return
Second call (key=abc) → found in store → return cached result, skip execution
```

### Key design decisions
- `IdempotencyStore<T>` is generic — reused for create ticket, assign ticket, any future operation
- Key namespaces: `create-ticket-{uuid}`, `assign-{ticketId}-{userId}`
- In production: Redis with 24h TTL instead of in-memory `ConcurrentHashMap`

---

## Step 10 — API Design

### Design Rules
1. **URLs are nouns** — `/tickets/{id}` not `/getTicketById`
2. **HTTP method is the verb** — GET=read, POST=create, PUT=update, DELETE=remove
3. **Nested resources** — `/tickets/{id}/comments` for comments on a ticket
4. **Correct status codes** — 201 on create, 404 on not found, 409 on conflict
5. **Versioned** — `/api/v1/` prefix so consumers aren't broken on schema changes
6. **Paginated lists** — `?page=0&size=20` on all list endpoints

### Endpoints
```
POST   /api/v1/tickets                      201 / 400        → create ticket
GET    /api/v1/tickets/{id}                 200 / 404        → get ticket
PUT    /api/v1/tickets/{id}/assign          200 / 404        → assign to user
PUT    /api/v1/tickets/{id}/status          200 / 404 / 409  → update status
POST   /api/v1/tickets/{id}/comments        201 / 404        → add comment
GET    /api/v1/tickets/{id}/comments        200 / 404        → get comments
GET    /api/v1/users/{id}/tickets           200 / 404        → user's tickets (paginated)
POST   /api/v1/users                        201 / 400        → create user
```

---

## Step 11 — Scaling & Edge Cases

### Edge Cases Handled
| Case | Handling |
|---|---|
| Concurrent ticket mutation | Optimistic locking — one thread wins, other retries |
| Duplicate API retry | Idempotency key — cached response returned |
| OPEN → CLOSED directly | Validation in `updateTicketStatus` throws `IllegalArgumentException` |
| Comment on unassigned ticket | `ticket.getUser()` returns null → notification skipped |
| Ticket not found | `Optional<Ticket>` — caller handles missing ticket explicitly |

### Scaling Considerations
| Component | Current | Production |
|---|---|---|
| Storage | `ConcurrentHashMap` (in-memory) | MySQL / MongoDB |
| Idempotency store | `ConcurrentHashMap` | Redis with TTL |
| Notifications | Synchronous in same thread | Async via message queue (Kafka/SQS) |
| Locking | Optimistic (AtomicInteger) | DB-level optimistic lock (`@Version`) |
| Multi-server | Single JVM only | Distributed lock (Redis Redlock) |

### What would break at scale
- `InMemoryTicketRepository` — data lost on restart, no shared state across servers
- Synchronous notifications — slow email/WhatsApp API blocks ticket operations
- `IdempotencyStore` in-memory — not shared across multiple server instances

---

## Sample Output

```
Retrieved Ticket: Login crash - NPE on login
Sending email to anmolsuri@gmail.com with message: Ticket assigned to user: Anmol Suri
Sending WhatsApp message to 1234 with message: Ticket assigned to user: Anmol Suri
First assignment — ticket assigned to: Anmol Suri
Duplicate request detected, returning cached ticket
Second assignment — ticket assigned to: Anmol Suri
```

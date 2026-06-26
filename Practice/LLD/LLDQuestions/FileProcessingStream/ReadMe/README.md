# Concurrency-Based File Processing Stream — Practice.LLD

---

## Step 1 — Requirements

### Functional Requirements
- Submit file path for processing (CSV, JSON, Log)
- Process multiple files concurrently via bounded worker pool
- Each file runs through sequential pipeline: validate → parse → transform
- Track job status per file: `PENDING → PROCESSING → COMPLETED / FAILED`
- Configurable failure mode per job: `FAIL_FAST` or `SKIP_AND_CONTINUE`
- Retry failed jobs up to per-job `maxRetries` limit
- Cancel in-progress jobs
- Prevent duplicate job creation on retry (idempotency)

### Non-Functional Requirements
- Bounded thread pool — no unbounded thread creation
- Back-pressure — block submitter when queue full
- Thread-safe status transitions
- Pluggable processors — add XML support without touching existing code
- Pluggable pipeline steps — add EncryptStep without changing pipeline
- Swappable storage (in-memory today, DB tomorrow)

---

## Step 2 — Entities / Models

| Entity | Key Fields | Purpose |
|---|---|---|
| `FileJob` | id, filePath, fileType, status, failureMode, maxRetries, retryCount, result | Represents one file processing job |
| `ProcessingResult` | recordsProcessed, errors, rawData (Map) | Output of a completed/failed job |
| `PipelineStep` (interface) | — | One step in the processing pipeline |
| `ValidationStep` | — | Checks file exists and is readable |
| `ParseStep` | — | Reads file, extracts records via FileParser |
| `TransformStep` | — | Cleans/normalizes parsed data |
| `FileParser` (interface) | — | File-type-specific parsing logic |
| `CsvFileParser` | — | Parses CSV files |
| `JsonFileParser` | — | Parses JSON files |
| `LogFileParser` | — | Parses log files |
| `FileParserFactory` | — | Returns correct FileParser for given FileType |
| `ProcessingPipeline` | steps: List\<PipelineStep\> | Executes steps in order, respects FailureMode |
| `FileWorker` | pipeline, queue, repository | Runnable — picks jobs from queue, runs pipeline |
| `WorkerPool` | threadPool, queue | Manages fixed thread pool of FileWorkers |
| `FileJobRepository` (interface) | — | CRUD for FileJob |
| `InMemoryFileJobRepository` | ConcurrentHashMap | In-memory storage |
| `IdempotencyStore<T>` | ConcurrentHashMap | Deduplication cache |
| `FileProcessingService` | workerPool, repository, queue, idempotencyStore | Entry point — submit, cancel, query |

### Enums
| Enum | Values |
|---|---|
| `FileStatus` | PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED |
| `FileType` | CSV, JSON, LOG |
| `FailureMode` | FAIL_FAST, SKIP_AND_CONTINUE |

---

## Step 3 — Schema & Relationships

```
FileProcessingService
  ├── owns WorkerPool
  ├── owns FileJobRepository
  ├── owns BlockingQueue<FileJob>
  └── owns IdempotencyStore<FileJob>

WorkerPool
  └── fixed thread pool of FileWorker threads
        └── each FileWorker consumes from BlockingQueue<FileJob>

FileWorker
  └── holds ProcessingPipeline

ProcessingPipeline
  └── holds List<PipelineStep>
        ├── ValidationStep
        ├── ParseStep ──── uses FileParserFactory ──► FileParser
        │                         ├── CsvFileParser
        │                         ├── JsonFileParser
        │                         └── LogFileParser
        └── TransformStep

FileJob (1) ──────────────── (1) ProcessingResult
FileJob ──── FileType (enum)
FileJob ──── FileStatus (AtomicReference)
FileJob ──── FailureMode (enum)

FileJobRepository (interface)
  └── InMemoryFileJobRepository (ConcurrentHashMap)

IdempotencyStore<T>
  └── ConcurrentHashMap<String, T>
```

---

## Step 4 — Package Structure

```
FileProcessingStream/
├── model/
│   ├── FileJob.java
│   ├── ProcessingResult.java
│   ├── FileStatus.java
│   ├── FileType.java
│   └── FailureMode.java
├── pipeline/
│   ├── PipelineStep.java              ← interface (Chain of Responsibility)
│   ├── ProcessingPipeline.java        ← executes steps in order
│   ├── ValidationStep.java
│   ├── ParseStep.java
│   └── TransformStep.java
├── parser/
│   ├── FileParser.java                ← interface (Strategy Pattern)
│   ├── FileParserFactory.java         ← Factory Pattern
│   ├── CsvFileParser.java
│   ├── JsonFileParser.java
│   └── LogFileParser.java
├── worker/
│   ├── FileWorker.java                ← Runnable, consumes queue
│   └── WorkerPool.java                ← ThreadPoolExecutor wrapper
├── repository/
│   ├── FileJobRepository.java         ← interface
│   └── InMemoryFileJobRepository.java
├── cache/
│   └── IdempotencyStore.java          ← generic cache
├── service/
│   └── FileProcessingService.java     ← main entry point
└── Main.java
```

---

## Step 5 — UML Class Diagram

```
┌──────────────────────────────────────────┐
│                 FileJob                  │
│──────────────────────────────────────────│
│ - id: String                             │
│ - filePath: String                       │
│ - fileType: FileType                     │
│ - status: AtomicReference<FileStatus>    │
│ - failureMode: FailureMode               │
│ - maxRetries: int                        │
│ - retryCount: AtomicInteger              │
│ - result: ProcessingResult               │
│──────────────────────────────────────────│
│ + getStatus(): FileStatus                │
│ + compareAndSetStatus(from, to): boolean │
│ + incrementRetry(): int                  │
└──────────────────────────────────────────┘

┌──────────────────────────────┐
│       ProcessingResult       │
│──────────────────────────────│
│ - recordsProcessed: int      │
│ - errors: List<String>       │
│ - rawData: Map<String,Object>│
└──────────────────────────────┘

PipelineStep (interface)
  + execute(job: FileJob): void
    △           △           △
    │           │           │
Validation   Parse      Transform
  Step        Step        Step

FileParser (interface)
  + parse(filePath: String): Map<String, Object>
    △              △              △
    │              │              │
CsvFileParser  JsonFileParser  LogFileParser

FileParserFactory
  + getParser(fileType: FileType): FileParser

┌──────────────────────────────────────┐
│           ProcessingPipeline         │
│──────────────────────────────────────│
│ - steps: List<PipelineStep>          │
│──────────────────────────────────────│
│ + execute(job: FileJob): void        │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│             FileWorker               │
│──────────────────────────────────────│
│ - pipeline: ProcessingPipeline       │
│ - queue: BlockingQueue<FileJob>      │
│ - repository: FileJobRepository      │
│──────────────────────────────────────│
│ + run(): void                        │
└──────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│           FileProcessingService              │
│──────────────────────────────────────────────│
│ - workerPool: WorkerPool                     │
│ - repository: FileJobRepository              │
│ - queue: BlockingQueue<FileJob>              │
│ - idempotencyStore: IdempotencyStore<FileJob>│
│──────────────────────────────────────────────│
│ + submit(filePath, fileType, failureMode,    │
│          maxRetries, idempotencyKey): FileJob│
│ + cancel(jobId): void                        │
│ + getJob(jobId): FileJob                     │
└──────────────────────────────────────────────┘

FileJobRepository (interface)
  + save / findById / update
    △
    │
InMemoryFileJobRepository (ConcurrentHashMap)

IdempotencyStore<T>
  + contains(key): boolean
  + save(key, value): void
  + get(key): T
```

---

## Step 6 — Design Patterns Used

### Strategy Pattern
`FileParser` interface — `CsvFileParser`, `JsonFileParser`, `LogFileParser` are interchangeable strategies. `ParseStep` delegates to correct parser via `FileParserFactory`. Adding XML = new class, zero changes elsewhere.

### Factory Pattern
`FileParserFactory.getParser(FileType)` — single lookup point. Caller never instantiates parsers directly.

### Chain of Responsibility
`ProcessingPipeline` holds ordered `List<PipelineStep>`. Each step processes job and passes to next. `FailureMode` controls whether chain breaks on error or continues.

### Producer-Consumer Pattern
| Role | Implementation |
|---|---|
| Producer | `FileProcessingService.submit()` puts jobs on queue |
| Queue | `ArrayBlockingQueue<FileJob>` — bounded, blocks when full |
| Consumer | `FileWorker` threads take from queue and process |

### Repository Pattern
`FileJobRepository` interface hides storage. Swap `InMemoryFileJobRepository` for `MySQLFileJobRepository` — service unchanged.

### Dependency Injection
`FileProcessingService` receives `WorkerPool`, `FileJobRepository`, `IdempotencyStore` via constructor. `Main.java` controls wiring.

---

## Step 7 — SOLID Principles

| Principle | Application |
|---|---|
| **S** Single Responsibility | `FileWorker` only processes jobs, `ProcessingPipeline` only orchestrates steps, `FileParserFactory` only resolves parsers |
| **O** Open/Closed | Add `XmlFileParser` — implement interface, register in factory. Add `EncryptStep` — implement `PipelineStep`, add to pipeline list. Zero changes to existing code |
| **L** Liskov Substitution | `CsvFileParser`/`JsonFileParser` interchangeable as `FileParser`. All `PipelineStep` implementations interchangeable |
| **I** Interface Segregation | `FileParser` has one method. `PipelineStep` has one method. `FileJobRepository` has focused CRUD only |
| **D** Dependency Inversion | `FileProcessingService` depends on `FileJobRepository` interface, not `InMemoryFileJobRepository` concretion |

---

## Step 8 — Concurrency

### Problem 1 — Status Transition Race
Worker sets `COMPLETED`. Cancel sets `CANCELLED` simultaneously. Last write wins silently.

### Solution — AtomicReference + compareAndSet
```
FileJob holds: AtomicReference<FileStatus> status

Cancel:
  compareAndSet(PROCESSING, CANCELLED)
  → true  = won, job cancelled
  → false = already terminal, throw 409 Conflict

Worker finish:
  compareAndSet(PROCESSING, COMPLETED)
  → true  = won, job completed
  → false = cancelled by user, skip result storage
```

### Why AtomicReference not volatile?
`volatile` guarantees visibility, not atomicity. Two threads can both read `PROCESSING` and both write — race still exists. `AtomicReference.compareAndSet` is atomic hardware instruction — exactly one thread wins.

### Problem 2 — Duplicate Submission
User submits same file twice quickly. Two identical jobs created, two workers process same file.

### Solution — IdempotencyStore
```
First call  (key=abc) → not in store → create job → save(key, job) → return job
Second call (key=abc) → found in store → return cached job, skip creation
```

### Problem 3 — Queue Back-Pressure
```
ArrayBlockingQueue(capacity=N)
  submit(): queue.put(job)  → blocks caller if full
  worker:   queue.take()    → blocks worker if empty
```

| Scenario | Approach |
|---|---|
| Low-medium load | Bounded BlockingQueue (this system) |
| High load / multi-server | External queue (Kafka/SQS) |
| HTTP caller blocked | Reject with 429, client retries |

---

## Step 9 — Idempotency

### Problem
Client calls `POST /files`, network drops, retries — two identical jobs created, file processed twice.

### Solution
Caller sends unique `Idempotency-Key` header. Service checks before creating job. If seen before, return cached result.

```
First call  (key=abc) → not in store → execute → save(key, job) → return
Second call (key=abc) → found in store → return cached job, skip execution
```

### Key design decisions
- `IdempotencyStore<T>` is generic — reused for any future idempotent operation
- Key in HTTP header (request metadata), not body (not part of job definition)
- Production: Redis with 24h TTL instead of in-memory `ConcurrentHashMap`

---

## Step 10 — API Design

### Design Rules
1. **URLs are nouns** — `/files/{id}` not `/getFile`
2. **HTTP method is verb** — POST=create, GET=read
3. **Correct status codes** — 201 on create, 404 on not found, 409 on conflict, 429 on queue full
4. **Versioned** — `/api/v1/` prefix
5. **Idempotency key in header** — follows Stripe convention

### Endpoints
```
POST   /api/v1/files                    201 / 400 / 429   → submit file job
       Header: Idempotency-Key: <uuid>
       Body:   { filePath, fileType, failureMode, maxRetries }

GET    /api/v1/files/{id}               200 / 404         → get job status + result

POST   /api/v1/files/{id}/cancel        200 / 404 / 409   → cancel job
                                                            409 if already COMPLETED/FAILED
```

---

## Step 11 — Scaling & Edge Cases

### Edge Cases Handled
| Case | Handling |
|---|---|
| File path does not exist | `ValidationStep` marks job `FAILED`, no retry (bad input) |
| Valid CSV but empty file | `COMPLETED` with `recordsProcessed=0` |
| Cancel already COMPLETED job | `compareAndSet` fails → 409 Conflict |
| Worker crashes mid-job | `try-catch` in `FileWorker.run()` → increment retryCount → re-queue if retries remain → else `FAILED` |
| Duplicate submission | `IdempotencyStore` returns cached job |
| Queue full | `BlockingQueue.put()` blocks submitter |
| Step fails + FAIL_FAST | Pipeline stops, job → `FAILED` |
| Step fails + SKIP_AND_CONTINUE | Error logged in result, next step runs |

### Scaling Considerations
| Component | Current | Production |
|---|---|---|
| Storage | `ConcurrentHashMap` (in-memory) | MySQL / PostgreSQL |
| Job queue | `ArrayBlockingQueue` (in-process) | Kafka / SQS |
| Idempotency store | `ConcurrentHashMap` | Redis with TTL |
| Thread pool | Fixed `ThreadPoolExecutor` | Tuned pool per load profile |
| Multi-server | Single JVM only | Shared external queue (Kafka/SQS) |
| Restart recovery | Jobs lost | Reload PENDING/PROCESSING from DB on startup |

### What breaks at scale
- `InMemoryFileJobRepository` — data lost on restart, not shared across servers
- In-process `BlockingQueue` — not visible to other server instances
- `IdempotencyStore` in-memory — duplicate jobs possible across multiple instances

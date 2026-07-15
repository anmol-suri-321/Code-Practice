Machine Coding: Task Orchestrator with DAG Execution
Duration: 90 minutes
Category: Concurrency + Graph Processing + Failure Handling

Real-World Context (Hevo Platform)
Hevo's data pipelines internally execute multi-step workflows: extract from source, transform, deduplicate, load to staging, merge to target, update offsets, fire webhooks. These steps have dependencies (e.g., merge cannot start before staging completes, webhooks fire only after merge). Today, many of these are sequential even when they could be parallelized. A DAG-based task orchestrator would allow defining these dependencies explicitly, executing independent steps in parallel, retrying transient failures, and resuming from the point of failure instead of re-running the entire pipeline.

Problem Statement
Design and implement a Task Orchestrator that executes tasks defined as a Directed Acyclic Graph (DAG). Tasks with all met dependencies should execute in parallel. The system must handle failures, retries, timeouts, and support re-execution from the failure point.

Requirements
Functional Requirements
DAG Validation - Detect cycles (throw exception), validate all dependency references exist

Parallel Execution - Tasks with all dependencies met should execute concurrently

Dependency Resolution - A task runs only after ALL its dependencies complete successfully

Timeout - Tasks exceeding timeoutMillis should be interrupted and marked TIMED_OUT

Retry with Backoff - Failed tasks retry up to maxRetries times with exponential backoff (100ms, 200ms, 400ms...)

Failure Propagation - When a task fails (after exhausting retries):

If continueOnFailure = false: all downstream dependents are marked FAILED.
If continueOnFailure = true: all downstream dependents scheduled like normal.
Resume from Failure - Given an ExecutionSnapshot, skip already-completed tasks and re-execute from the failure point

Example Scenario
DAG Structure
A (extract)
/ \
B   C (transform-1, transform-2)
|   |
D   E (stage-1, stage-2)
\ /
F (merge)
|
G (webhook)
Execution Flow
Time 0:   A starts (no dependencies)
Time 1:   A completes -> B and C start in parallel
Time 2:   B completes, C still running -> D starts
Time 3:   C fails (transient error) -> retry 1 after 100ms
Time 3.1: C retry 1 succeeds -> E starts
Time 4:   D completes, E completes -> F starts
Time 5:   F completes -> G starts
Time 6:   G completes -> ExecutionResult(COMPLETED)
Failure Scenario (no retries left)
Time 0:   A completes -> B, C start
Time 1:   B completes -> D starts
Time 2:   C fails, maxRetries=0, continueOnFailure=false
-> E marked FAILED, F marked FAILED, G marked FAILED
Time 3:   D completes (was already running)
Result:   PARTIAL_FAILURE
Snapshot: {A: COMPLETED, B: COMPLETED, C: FAILED, D: COMPLETED, E: FAILED, F: FAILED, G: FAILED}

Resume:   orchestrator.resumeFrom(dag, snapshot)
-> Skips A, B, D (already completed)
-> Re-executes C
-> If C succeeds, executes E, then F, then G
Sample Driver Code and Test Case 1
The candidate is expected to design all classes, interfaces, and enums themselves. The driver below uses string-based inputs only. No predefined types are given. The candidate should parse these inputs and build their own abstractions.

public class Main {
public static void main(String[] args) {

        // ============================================================
        // TEST CASE 1
        // DAG:
        //
        //         A
        //        / \
        //       B   C
        //       |   |
        //       D   E
        //        \ /
        //         F
        //         |
        //         G
        //
        // C fails on first attempt and succeeds on retry.
        // F fails with no retries.
        // ============================================================

        String[][] taskDefinitions = {
            {"A", "2", "5000", "false"},
            {"B", "1", "3000", "false"},
            {"C", "1", "3000", "true"},
            {"D", "0", "10000", "false"},
            {"E", "0", "10000", "false"},
            {"F", "0", "30000", "false"},
            {"G", "2", "5000", "true"}
        };

        String[][] dependencies = {
            {"A", "B"},
            {"A", "C"},
            {"B", "D"},
            {"C", "E"},
            {"D", "F"},
            {"E", "F"},
            {"F", "G"}
        };

        // ============================================================
        // EXPECTED OUTPUT (Run 1)
        //
        // Executing A...
        // A -> COMPLETED
        //
        // Executing B...
        // Executing C...
        //
        // C -> FAILED (attempt 1)
        // Retrying...
        //
        // B -> COMPLETED
        // C -> COMPLETED
        //
        // Executing D...
        // Executing E...
        //
        // D -> COMPLETED
        // E -> COMPLETED
        //
        // Executing F...
        // F -> FAILED
        //
        // G -> FAILED
        //
        // ===== Execution Summary =====
        //
        // A : COMPLETED
        // B : COMPLETED
        // C : COMPLETED
        // D : COMPLETED
        // E : COMPLETED
        // F : FAILED
        // G : FAILED
        //
        // Overall : PARTIAL_FAILURE
        //
        // ============================================================
        // EXPECTED OUTPUT (Resume)
        //
        // Skipping A
        // Skipping B
        // Skipping C
        // Skipping D
        // Skipping E
        //
        // Executing F...
        // F -> COMPLETED
        //
        // Executing G...
        // G -> COMPLETED
        //
        // ===== Execution Summary =====
        //
        // A : COMPLETED
        // B : COMPLETED
        // C : COMPLETED
        // D : COMPLETED
        // E : COMPLETED
        // F : COMPLETED
        // G : COMPLETED
        //
        // Overall : COMPLETED
        // ============================================================


        // ============================================================
        // TEST CASE 2
        // Linear Chain
        //
        // X -> Y -> Z
        //
        // All tasks succeed.
        // ============================================================

        String[][] taskDefinitions2 = {
            {"X", "0", "5000", "false"},
            {"Y", "0", "5000", "false"},
            {"Z", "0", "5000", "false"}
        };

        String[][] dependencies2 = {
            {"X", "Y"},
            {"Y", "Z"}
        };

        // Expected:
        //
        // Executing X...
        // X -> COMPLETED
        //
        // Executing Y...
        // Y -> COMPLETED
        //
        // Executing Z...
        // Z -> COMPLETED
        //
        // ===== Execution Summary =====
        // X : COMPLETED
        // Y : COMPLETED
        // Z : COMPLETED
        //
        // Overall : COMPLETED


        // ============================================================
        // TEST CASE 3
        // Cycle Detection
        //
        // P -> Q -> R -> P
        // ============================================================

        String[][] taskDefinitions3 = {
            {"P", "0", "5000", "false"},
            {"Q", "0", "5000", "false"},
            {"R", "0", "5000", "false"}
        };

        String[][] dependencies3 = {
            {"P", "Q"},
            {"Q", "R"},
            {"R", "P"}
        };

        // Expected:
        //
        // DAG validation failed:
        // Cycle detected.
        //
        // No task should execute.


        // ============================================================
        // TEST CASE 4
        //
        //            A
        //         /  |  \
        //        B   C   D
        //        |   |   |
        //        E   F   G
        //         \  |  /
        //           H
        //           |
        //           I
        //
        // B fails (continueOnFailure=true)
        // -> E becomes SKIPPED
        //
        // D fails (continueOnFailure=false)
        // -> G becomes FAILED
        // -> H becomes FAILED
        // -> I becomes FAILED
        // ============================================================

        String[][] taskDefinitions4 = {
            {"A", "0", "5000", "false"},
            {"B", "0", "3000", "true"},
            {"C", "0", "3000", "false"},
            {"D", "0", "3000", "false"},
            {"E", "0", "5000", "false"},
            {"F", "0", "5000", "false"},
            {"G", "0", "5000", "false"},
            {"H", "0", "10000", "false"},
            {"I", "0", "5000", "false"}
        };

        String[][] dependencies4 = {
            {"A", "B"},
            {"A", "C"},
            {"A", "D"},
            {"B", "E"},
            {"C", "F"},
            {"D", "G"},
            {"E", "H"},
            {"F", "H"},
            {"G", "H"},
            {"H", "I"}
        };

        // Expected:
        //
        // Executing A...
        // A -> COMPLETED
        //
        // Executing B...
        // Executing C...
        // Executing D...
        //
        // B -> FAILED
        // E -> SKIPPED
        //
        // C -> COMPLETED
        //
        // Executing F...
        // F -> COMPLETED
        //
        // D -> FAILED
        //
        // G -> FAILED
        // H -> FAILED
        // I -> FAILED
        //
        // ===== Execution Summary =====
        //
        // A : COMPLETED
        // B : FAILED
        // C : COMPLETED
        // D : FAILED
        // E : SKIPPED
        // F : COMPLETED
        // G : FAILED
        // H : FAILED
        // I : FAILED
        //
        // Overall : PARTIAL_FAILURE

        // ------------------------------------------------------------
        // Candidate's code starts here.
        // ------------------------------------------------------------
    }
}

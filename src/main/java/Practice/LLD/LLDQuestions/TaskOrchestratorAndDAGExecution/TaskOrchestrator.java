package Practice.LLD.LLDQuestions.TaskOrchestratorAndDAGExecution;

import java.util.*;

public class TaskOrchestrator {
    Map<String, List<String>> graph = new HashMap<>();
    private final Map<String, Integer> indegree = new HashMap<>();
    private final List<Task> tasks;
    private final Map<String, Task> taskMap = new HashMap<>();

    public TaskOrchestrator(String[][] taskDefinitions, String[][] dependencies) {
        this.tasks = createTasksList(taskDefinitions);

        buildgraph(dependencies);
    }

    public void execute() {
        checkIfCycleExists(graph);

        Queue<String> queue = new LinkedList<>();

        for(String id : indegree.keySet()) {
            if(indegree.getOrDefault(id, 0) == 0) {
                queue.offer(id);
            }
        }

        while(!queue.isEmpty()) {
            String taskId = queue.poll();
            Task task = taskMap.get(taskId);

            // execute this task
            boolean continueOnFailure = executeTask(task);
            System.out.println(taskId + "-> COMPLETED");

            if(continueOnFailure == false) {

            }

            for(String nextTask : graph.getOrDefault(taskId, Collections.emptyList())) {
                indegree.put(nextTask, indegree.getOrDefault(nextTask, 0) - 1);

                if(indegree.get(nextTask) == 0) {
                    queue.offer(nextTask);
                }
            }
        }
    }

    public boolean executeTask(Task task) {
        String id = task.getId();
        int retries = task.getRetries();
        int timeOutinMillis = task.getTimeOutinMillis();
        boolean continueOnFailure = task.isContinueOnFailure();
        System.out.println("Executing " + id);

        for(int retry = 1; retry <= retries; retry++) {
            try {
                Thread.sleep(3001);
            } catch (Exception e) {
                System.out.println(id + " -> FAILED " + "(attempt " + retry + ")");
                return continueOnFailure == true;
            }
        }

        return true;
    }

    private List<Task> createTasksList(String[][] taskDefinitions) {
        List<Task> tasks = new ArrayList<>();
        for(String[] s : taskDefinitions) {
            String id = s[0];
            int retries = Integer.parseInt(s[1]);
            int timeOutinMillis = Integer.parseInt(s[2]);
            boolean continueOnFailure = Boolean.getBoolean(s[3]);
            Task task = new Task(id, retries, timeOutinMillis, continueOnFailure);
            tasks.add(task);
            taskMap.put(id, task);
        }

        return tasks;
    }

    private void buildgraph(String[][] dependencies) {
        for(String[] s : dependencies) {
            String u = s[0];
            String v = s[1];
            indegree.putIfAbsent(v, indegree.getOrDefault(v, 0) + 1);
            graph.putIfAbsent(u, new ArrayList<>());
            graph.get(u).add(v);
        }
    }


    private static void checkIfCycleExists(Map<String, List<String>> graph) {
        Set<String> vis = new HashSet<>();
        Set<String> currVis = new HashSet<>();

        for(String u : graph.keySet()) {
            if(!vis.contains(u)) {
                dfs(u, graph, vis, currVis);
            }
        }
    }

    private static void dfs(String u, Map<String, List<String>> graph, Set<String> vis, Set<String> currVis) {
        if(currVis.contains(u)) {
            throw new RuntimeException("Cycle detected in the Graph");
        }

        if(vis.contains(u)) {
            return;
        }

        vis.add(u);
        currVis.add(u);

        for(String v : graph.getOrDefault(u, Collections.emptyList())) {
            dfs(v, graph, vis, currVis);
        }

        currVis.remove(u);
    }
}

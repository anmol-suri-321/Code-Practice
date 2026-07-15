package Practice.LLD.LLDQuestions.TaskOrchestratorAndDAGExecution;

public class Main {
    public static void main(String[] args) {
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
                {"A", "D"},
                {"B", "E"},
                {"C", "F"},
                {"D", "G"},
                {"E", "H"},
                {"F", "H"},
                {"G", "H"},
                {"H", "I"}
        };

        TaskOrchestrator orchestrator = new TaskOrchestrator(taskDefinitions, dependencies);
        orchestrator.execute();

    }


}

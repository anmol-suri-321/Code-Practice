package Practice.LLD.LLDQuestions.JiraTicketSystem;

import Practice.LLD.LLDQuestions.JiraTicketSystem.Ticket.Ticket;

import java.util.List;

public class Project {
    String projectId;
    String name;
    String description;
    List<Ticket> tickets;

    public Project(String projectId, String name, String description, List<Ticket> tickets) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.tickets = tickets;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}

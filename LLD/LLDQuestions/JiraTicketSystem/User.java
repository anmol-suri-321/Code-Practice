package LLD.LLDQuestions.JiraTicketSystem;

import java.util.List;

public class User {
    String id;
    String name;
    List<Ticket> assignedTickets;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    void assignTicket(Ticket ticket) {
        assignedTickets.add(ticket);
    }

    void removeTicket(Ticket ticket) {
        assignedTickets.remove(ticket);
    }
}

package LLD.LLDQuestions.JiraTicketSystem;

import LLD.LLDQuestions.JiraTicketSystem.Ticket.Ticket;

import java.util.ArrayList;
import java.util.List;

public class User {
    String id;
    String name;
    String email;
    String phoneNumber;
    List<Ticket> assignedTickets;

    public User(String id, String name, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.assignedTickets = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    void assignTicket(Ticket ticket) {
        assignedTickets.add(ticket);
    }

    void removeTicket(Ticket ticket) {
        assignedTickets.remove(ticket);
    }

    public List<Ticket> getAssignedTickets() {
        return assignedTickets;
    }
}

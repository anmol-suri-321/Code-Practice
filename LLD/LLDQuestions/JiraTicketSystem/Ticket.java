package LLD.LLDQuestions.JiraTicketSystem;

public class Ticket {
    String id;
    String name;
    String description;
    TicketStatus ticketStatus;
    Priority priority;
    User user;

    public Ticket(String id, String name, String description, TicketStatus ticketStatus, Priority priority) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ticketStatus = ticketStatus;
        this.priority = priority;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void assignTicket(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

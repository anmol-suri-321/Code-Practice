package Practice.LLD.LLDQuestions.JiraTicketSystem.Ticket;

import Practice.LLD.LLDQuestions.JiraTicketSystem.Priority;

public class TicketRequest {
    String id;
    String name;
    String description;
    Priority priority;
    TicketType ticketType;
    BugSeverity bugSeverity;
    TshirtSize tshirtSize;

    public TicketRequest(String id, String name, String description, Priority priority, TicketType ticketType, BugSeverity bugSeverity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.ticketType = ticketType;
        this.bugSeverity = bugSeverity;
        this.tshirtSize = null;
    }

    public TicketRequest(String id, String name, String description, Priority priority, TicketType ticketType, TshirtSize tshirtSize) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.ticketType = ticketType;
        this.bugSeverity = null;
        this.tshirtSize = tshirtSize;
    }
}

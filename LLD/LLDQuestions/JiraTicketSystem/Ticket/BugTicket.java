package LLD.LLDQuestions.JiraTicketSystem.Ticket;

import LLD.LLDQuestions.JiraTicketSystem.Priority;
import LLD.LLDQuestions.JiraTicketSystem.TicketStatus;

public class BugTicket extends Ticket {
    private BugSeverity bugSeverity;

    public BugTicket(String id, String name, String description, TicketStatus ticketStatus, Priority priority, BugSeverity severity) {
        super(id, name, description, ticketStatus, priority);
        this.bugSeverity = severity;
    }

    @Override
    public TicketType getTicketType() {
        return TicketType.BUG;
    }

    public BugSeverity getBugSeverity() {
        return bugSeverity;
    }
}

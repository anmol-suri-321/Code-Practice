package LLD.LLDQuestions.JiraTicketSystem.Ticket;

import LLD.LLDQuestions.JiraTicketSystem.Priority;
import LLD.LLDQuestions.JiraTicketSystem.TicketStatus;

public class TicketFactory {

    public static Ticket createTicket(TicketRequest ticketRequest) {
        TicketType ticketType = ticketRequest.ticketType;
        String id = ticketRequest.id;
        String name = ticketRequest.name;
        String description = ticketRequest.description;
        TicketStatus ticketStatus = TicketStatus.OPEN; // Default status for new tickets
        Priority priority = ticketRequest.priority;
        return switch (ticketType) {
            case BUG -> new BugTicket(id, name, description, ticketStatus, priority, ticketRequest.bugSeverity);
            case STORY -> new StoryTicket(id, name, description, ticketStatus, priority, ticketRequest.tshirtSize);
            default -> throw new UnsupportedOperationException("Ticket type not supported: " + ticketType);
        };
    }
}

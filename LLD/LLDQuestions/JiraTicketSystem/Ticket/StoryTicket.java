package LLD.LLDQuestions.JiraTicketSystem.Ticket;

import LLD.LLDQuestions.JiraTicketSystem.Priority;
import LLD.LLDQuestions.JiraTicketSystem.TicketStatus;

public class StoryTicket extends Ticket {
    private TshirtSize tshirtSize;

    public StoryTicket(String id, String name, String description, TicketStatus ticketStatus, Priority priority, TshirtSize size) {
        super(id, name, description, ticketStatus, priority);
        this.tshirtSize = size;
    }

    @Override
    public TicketType getTicketType() {
        return TicketType.STORY;
    }

    public TshirtSize getTshirtSize() {
        return tshirtSize;
    }
}

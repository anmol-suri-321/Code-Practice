package LLD.LLDQuestions.JiraTicketSystem;

import java.util.List;

public class TicketService {

    public TicketService() {}

    public void assignTicketToUser(Ticket ticket, User user) {
        user.assignTicket(ticket);
    }

    public List<Ticket> getTicketsForUser(User user) {
        return user.getAssignedTickets();
    }

    public void updateTicketStatus(Ticket ticket, TicketStatus newStatus) {
        TicketStatus currentStatus = ticket.getTicketStatus();
        // A ticket can't be closed if It's not yet in progress
        if(currentStatus.equals(TicketStatus.OPEN) && newStatus.equals(TicketStatus.CLOSED)) {
                throw new IllegalArgumentException("A ticket can't be closed if It's not yet in progress");
        }

        ticket.setTicketStatus(newStatus);
    }
}

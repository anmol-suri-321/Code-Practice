package LLD.LLDQuestions.JiraTicketSystem.Ticket;

import LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationEvent;
import LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationEventType;
import LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationService;
import LLD.LLDQuestions.JiraTicketSystem.User;

import java.util.List;

public class TicketService {
    NotificationService notificationService;

    public TicketService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void assignTicketToUser(Ticket ticket, User user) {
        int currentVersion = ticket.getVersion();
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        if (ticket.compareAndIncrementVersion(currentVersion)) {
            throw new RuntimeException("Concurrent modification detected on ticket: " + ticket.getId());
        }

        ticket.assignTicket(user);
        user.assignTicket(ticket);

        notificationService.notify(
                new NotificationEvent(
                        NotificationEventType.TICKET_ASSIGNED,
                        ticket,
                        user,
                        "Ticket assigned to user: " + user.getName()));
    }

    public List<Ticket> getTicketsForUser(User user) {
        return user.getAssignedTickets();
    }

    public void updateTicketStatus(Ticket ticket, TicketStatus newStatus) {
        int currentVersion = ticket.getVersion();

        TicketStatus currentStatus = ticket.getTicketStatus();
        // A ticket can't be closed if It's not yet in progress
        if(currentStatus.equals(TicketStatus.OPEN) && newStatus.equals(TicketStatus.CLOSED)) {
                throw new IllegalArgumentException("A ticket can't be closed if It's not yet in progress");
        }

        if (ticket.compareAndIncrementVersion(currentVersion)) {
            throw new RuntimeException("Concurrent modification detected on ticket: " + ticket.getId());
        }

        ticket.setTicketStatus(newStatus);
        notificationService.notify(
                new NotificationEvent(
                        NotificationEventType.STATUS_CHANGED,
                        ticket,
                        ticket.getUser(),
                        "Ticket status updated to: " + newStatus));
    }
}

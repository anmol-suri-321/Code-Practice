package LLD.LLDQuestions.JiraTicketSystem.Ticket;

import LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationEvent;
import LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationEventType;
import LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationService;
import LLD.LLDQuestions.JiraTicketSystem.User;
import LLD.LLDQuestions.JiraTicketSystem.cache.IdempotencyStore;

import java.util.List;

public class TicketService {
    NotificationService notificationService;
    IdempotencyStore<Ticket> idempotencyStore;

    public TicketService(NotificationService notificationService, IdempotencyStore<Ticket> idempotencyStore) {
        this.notificationService = notificationService;
        this.idempotencyStore = idempotencyStore;
    }

    public void assignTicketToUser(Ticket ticket, User user, String idempotencyKey) {
        if (idempotencyStore.contains(idempotencyKey)) {
            System.out.println("Duplicate request detected, returning cached ticket");
            return;
        }
        int currentVersion = ticket.getVersion();

        if (ticket.compareAndIncrementVersion(currentVersion)) {
            throw new RuntimeException("Concurrent modification detected on ticket: " + ticket.getId());
        }

        ticket.assignTicket(user);
        user.assignTicket(ticket);
        idempotencyStore.save(idempotencyKey, ticket);

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

    public Ticket createTicket(TicketRequest request, String idempotencyKey) {
        if (idempotencyStore.contains(idempotencyKey)) {
            System.out.println("Duplicate request detected, returning cached ticket");
            return idempotencyStore.get(idempotencyKey);
        }
        Ticket ticket = TicketFactory.createTicket(request);
        idempotencyStore.save(idempotencyKey, ticket);
        return ticket;
    }
}

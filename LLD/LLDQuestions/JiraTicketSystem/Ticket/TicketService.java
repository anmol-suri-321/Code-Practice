package LLD.LLDQuestions.JiraTicketSystem.Ticket;

import LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationEvent;
import LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationEventType;
import LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationService;
import LLD.LLDQuestions.JiraTicketSystem.User;
import LLD.LLDQuestions.JiraTicketSystem.cache.IdempotencyStore;
import LLD.LLDQuestions.JiraTicketSystem.repository.TicketRepository;

import java.util.List;
import java.util.Optional;

public class TicketService {
    NotificationService notificationService;
    IdempotencyStore<Ticket> idempotencyStore;
    TicketRepository ticketRepository;

    public TicketService(NotificationService notificationService, IdempotencyStore<Ticket> idempotencyStore, TicketRepository ticketRepository) {
        this.notificationService = notificationService;
        this.idempotencyStore = idempotencyStore;
        this.ticketRepository = ticketRepository;
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
        ticketRepository.update(ticket);
        idempotencyStore.save(idempotencyKey, ticket);

        notificationService.notify(
                new NotificationEvent(
                        NotificationEventType.TICKET_ASSIGNED,
                        ticket,
                        user,
                        "Ticket assigned to user: " + user.getName()));
    }

    public List<Ticket> getTicketsForUser(User user) {
        return ticketRepository.findByUser(user);
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
        ticketRepository.update(ticket);
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
        ticketRepository.save(ticket);
        idempotencyStore.save(idempotencyKey, ticket);
        return ticket;
    }

    public Optional<Ticket> findById(String id) {
        return ticketRepository.findById(id);
    }
}

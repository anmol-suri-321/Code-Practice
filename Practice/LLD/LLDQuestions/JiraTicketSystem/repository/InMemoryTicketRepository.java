package Practice.LLD.LLDQuestions.JiraTicketSystem.repository;

import Practice.LLD.LLDQuestions.JiraTicketSystem.Ticket.Ticket;
import Practice.LLD.LLDQuestions.JiraTicketSystem.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTicketRepository implements TicketRepository {
    Map<String, Ticket> ticketMap = new ConcurrentHashMap<>();
    @Override
    public void save(Ticket ticket) {
        ticketMap.put(ticket.getId(), ticket);
    }

    @Override
    public Optional<Ticket> findById(String ticketId) {
        return Optional.ofNullable(ticketMap.get(ticketId));
    }

    @Override
    public void update(Ticket ticket) {
        ticketMap.put(ticket.getId(), ticket);
    }

    @Override
    public void delete(Ticket ticket) {
        ticketMap.remove(ticket.getId());
    }

    @Override
    public List<Ticket> findByUser(User user) {
        List<Ticket> tickets = new ArrayList<>();
        for (Ticket ticket : ticketMap.values()) {
            if (ticket.getUser() != null && ticket.getUser().getId().equals(user.getId())) {
                tickets.add(ticket);
            }
        }
        return tickets;
    }
}

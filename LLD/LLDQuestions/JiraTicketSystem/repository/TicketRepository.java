package LLD.LLDQuestions.JiraTicketSystem.repository;

import LLD.LLDQuestions.JiraTicketSystem.Ticket.Ticket;
import LLD.LLDQuestions.JiraTicketSystem.User;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    void save(Ticket ticket);
    Optional<Ticket> findById(String ticketId);
    void update(Ticket ticket);
    void delete(Ticket ticket);
    List<Ticket> findByUser(User user);
}

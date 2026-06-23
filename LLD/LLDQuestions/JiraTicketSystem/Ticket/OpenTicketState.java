package LLD.LLDQuestions.JiraTicketSystem.Ticket;

import LLD.LLDQuestions.JiraTicketSystem.Comment.Comment;
import LLD.LLDQuestions.JiraTicketSystem.Priority;
import LLD.LLDQuestions.JiraTicketSystem.User;

public class OpenTicketState implements TicketState {
    @Override
    public void addComment(Ticket ticket, Comment comment) {
        ticket.getComments().add(comment);
    }

    @Override
    public void changePriority(Ticket ticket, Priority priority) {
        ticket.setPriority(priority);
    }

    @Override
    public void assignUser(Ticket ticket, User user) {
        ticket.assignTicket(user);
    }

    @Override
    public void updateStatus(Ticket ticket, TicketStatus newStatus) {
        if (newStatus == TicketStatus.CLOSED || newStatus == TicketStatus.IN_PROGRESS) {
            ticket.setTicketStatus(newStatus);
            ticket.setCurrentState(newStatus);
        } else {
            throw new IllegalStateException("Invalid status transition from OPEN to " + newStatus);
        }
    }
}

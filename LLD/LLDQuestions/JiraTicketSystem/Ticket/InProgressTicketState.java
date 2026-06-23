package LLD.LLDQuestions.JiraTicketSystem.Ticket;

import LLD.LLDQuestions.JiraTicketSystem.Comment.Comment;
import LLD.LLDQuestions.JiraTicketSystem.Priority;
import LLD.LLDQuestions.JiraTicketSystem.User;

public class InProgressTicketState implements TicketState {
    @Override
    public void addComment(Ticket ticket, Comment comment) {
        ticket.addComment(comment);
    }

    @Override
    public void changePriority(Ticket ticket, Priority priority) {
        ticket.setPriority(priority);
    }

    @Override
    public void assignUser(Ticket ticket, User user) {
        throw new IllegalStateException("Cannot assign user to a ticket in progress.");
    }

    @Override
    public void updateStatus(Ticket ticket, TicketStatus newStatus) {
        if (newStatus == TicketStatus.CLOSED || newStatus == TicketStatus.OPEN) {
            ticket.setTicketStatus(newStatus);
            ticket.setCurrentState(newStatus);
        } else {
            throw new IllegalStateException("Invalid status transition from IN_PROGRESS to " + newStatus);
        }

    }
}

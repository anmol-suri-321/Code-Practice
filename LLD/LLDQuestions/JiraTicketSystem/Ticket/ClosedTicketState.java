package LLD.LLDQuestions.JiraTicketSystem.Ticket;

import LLD.LLDQuestions.JiraTicketSystem.Comment.Comment;
import LLD.LLDQuestions.JiraTicketSystem.Priority;
import LLD.LLDQuestions.JiraTicketSystem.User;

public class ClosedTicketState implements TicketState {

    @Override
    public void addComment(Ticket ticket, Comment comment) {
        throw new IllegalStateException("Cannot add comment to a closed ticket.");
    }

    @Override
    public void changePriority(Ticket ticket, Priority priority) {
        throw new IllegalStateException("Cannot change priority of a closed ticket.");
    }

    @Override
    public void assignUser(Ticket ticket, User user) {
        throw new IllegalStateException("Cannot assign user to a closed ticket.");
    }

    @Override
    public void updateStatus(Ticket ticket, TicketStatus newStatus) {
        if (newStatus == TicketStatus.OPEN || newStatus == TicketStatus.IN_PROGRESS) {
            ticket.setTicketStatus(newStatus);
            ticket.setCurrentState(newStatus);
        } else {
            throw new IllegalStateException("Invalid status transition from CLOSED to " + newStatus);
        }
    }
}

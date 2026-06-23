package LLD.LLDQuestions.JiraTicketSystem.Ticket;

import LLD.LLDQuestions.JiraTicketSystem.Comment.Comment;
import LLD.LLDQuestions.JiraTicketSystem.Priority;
import LLD.LLDQuestions.JiraTicketSystem.User;

public interface TicketState {
    void addComment(Ticket ticket, Comment comment);

    void changePriority(Ticket ticket, Priority priority);

    void assignUser(Ticket ticket, User user);

    void updateStatus(Ticket ticket, TicketStatus newStatus);
}

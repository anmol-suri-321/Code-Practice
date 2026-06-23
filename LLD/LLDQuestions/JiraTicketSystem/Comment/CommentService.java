package LLD.LLDQuestions.JiraTicketSystem.Comment;

import LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationEvent;
import LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationEventType;
import LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationService;
import LLD.LLDQuestions.JiraTicketSystem.Ticket.Ticket;
import LLD.LLDQuestions.JiraTicketSystem.User;

public class CommentService {
    NotificationService notificationService;

    public CommentService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    void addComment(Ticket ticket, Comment comment) {
        User user = ticket.getUser();
        ticket.addComment(comment);
        notificationService.notify(
                new NotificationEvent(
                        NotificationEventType.COMMENT_ADDED,
                        ticket,
                        user,
                        "New comment added to ticket: " + comment.getMessage()));
    }
}

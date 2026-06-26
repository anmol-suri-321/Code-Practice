package Practice.LLD.LLDQuestions.JiraTicketSystem.Comment;

import Practice.LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationEvent;
import Practice.LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationEventType;
import Practice.LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationService;
import Practice.LLD.LLDQuestions.JiraTicketSystem.Ticket.Ticket;
import Practice.LLD.LLDQuestions.JiraTicketSystem.User;

public class CommentService {
    NotificationService notificationService;

    public CommentService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void addComment(Ticket ticket, Comment comment) {
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

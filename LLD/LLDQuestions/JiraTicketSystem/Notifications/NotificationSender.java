package LLD.LLDQuestions.JiraTicketSystem.Notifications;

public interface NotificationSender {
    void send(NotificationEvent event);
}

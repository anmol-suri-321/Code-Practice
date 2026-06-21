package LLD.LLDQuestions.JiraTicketSystem.Notifications;

public class EmailNotificationSender implements NotificationSender {
    @Override
    public void send(NotificationEvent event) {
        System.out.println("Sending email to " + event.getUser().getEmail() + " with message: " + event.getMessage());
    }
}

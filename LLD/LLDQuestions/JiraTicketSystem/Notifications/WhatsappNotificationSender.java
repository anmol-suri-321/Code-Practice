package LLD.LLDQuestions.JiraTicketSystem.Notifications;

public class WhatsappNotificationSender implements NotificationSender {
    @Override
    public void send(NotificationEvent event) {
        System.out.println("Sending WhatsApp message to " + event.getUser().getPhoneNumber() + " with message: " + event.getMessage());
    }
}

package Practice.LLD.LLDQuestions.JiraTicketSystem.Notifications;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private List<NotificationSender> notificationSenders;

    public NotificationService() {
        this.notificationSenders = new ArrayList<>();
    }

    public void addNotificationSender(NotificationSender notificationSender) {
        notificationSenders.add(notificationSender);
    }

    public void notify(NotificationEvent notificationEvent) {
        for(NotificationSender notificationSender : notificationSenders) {
            notificationSender.send(notificationEvent);
        }
    }
}

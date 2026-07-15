package Practice.LLD.LLDQuestions.JiraTicketSystem.Notifications;

import Practice.LLD.LLDQuestions.JiraTicketSystem.Ticket.Ticket;
import Practice.LLD.LLDQuestions.JiraTicketSystem.User;

public class NotificationEvent {
    NotificationEventType eventType;
    Ticket ticket;
    User user;
    String message;


    public NotificationEvent(NotificationEventType eventType, Ticket ticket, User user, String message) {
        this.eventType = eventType;
        this.ticket = ticket;
        this.user = user;
        this.message = message;
    }


    public NotificationEventType getEventType() {
        return eventType;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}

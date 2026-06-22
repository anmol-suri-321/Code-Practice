package LLD.LLDQuestions.JiraTicketSystem;

import LLD.LLDQuestions.JiraTicketSystem.Notifications.EmailNotificationSender;
import LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationService;
import LLD.LLDQuestions.JiraTicketSystem.Notifications.WhatsappNotificationSender;
import LLD.LLDQuestions.JiraTicketSystem.Ticket.*;

public class Main {
    public static void main(String[] args) {
        // Create Notifications Service
        NotificationService notificationService = new NotificationService();

        TicketService ticketService = new TicketService(notificationService);

        notificationService.addNotificationSender(new EmailNotificationSender());
        notificationService.addNotificationSender(new WhatsappNotificationSender());

        User user1 = new User("1", "Anmol Suri", "anmolsuri1@gmail.com", "12345");
        User user2 = new User("2", "Anmol Suri 2", "asuri2@gmail.com", "23456");

        Ticket ticket1 = new BugTicket("1", "Ticket 1", "Description for ticket 1", TicketStatus.OPEN, Priority.HIGH, BugSeverity.CRITICAL);
        Ticket ticket2 = new StoryTicket("2", "Ticket 2", "Description for ticket 2", TicketStatus.OPEN, Priority.MEDIUM, TshirtSize.XL);

        ticketService.assignTicketToUser(ticket1, user1);
        ticketService.assignTicketToUser(ticket2, user1);
        ticketService.updateTicketStatus(ticket1, TicketStatus.IN_PROGRESS);
        ticketService.updateTicketStatus(ticket1, TicketStatus.CLOSED);

        ticketService.getTicketsForUser(user1).
                forEach(ticket -> System.out.println("User 1 Ticket: " + ticket.getName() + " - " + ticket.getTicketStatus()));
        ticketService.getTicketsForUser(user2).
                forEach(ticket -> System.out.println("User 2 Ticket: " + ticket.getName() + " - " + ticket.getTicketStatus()));
    }
}

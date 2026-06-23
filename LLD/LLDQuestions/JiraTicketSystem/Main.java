package LLD.LLDQuestions.JiraTicketSystem;

import LLD.LLDQuestions.JiraTicketSystem.Comment.Comment;
import LLD.LLDQuestions.JiraTicketSystem.Comment.CommentService;
import LLD.LLDQuestions.JiraTicketSystem.Notifications.EmailNotificationSender;
import LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationService;
import LLD.LLDQuestions.JiraTicketSystem.Notifications.WhatsappNotificationSender;
import LLD.LLDQuestions.JiraTicketSystem.Ticket.*;

public class Main {
    public static void main(String[] args) {
        // Create Notifications Service
        NotificationService notificationService = new NotificationService();

        TicketService ticketService = new TicketService(notificationService);
        CommentService commentService = new CommentService(notificationService);

        notificationService.addNotificationSender(new EmailNotificationSender());
        notificationService.addNotificationSender(new WhatsappNotificationSender());

        User user1 = new User("1", "Anmol Suri", "anmolsuri1@gmail.com", "12345");
        User user2 = new User("2", "Anmol Suri 2", "asuri2@gmail.com", "23456");

        TicketRequest ticketRequest = new TicketRequest("1", "Ticket 1", "Description for ticket 1", Priority.HIGH, TicketType.BUG, BugSeverity.CRITICAL);
        Ticket ticket1 = TicketFactory.createTicket(ticketRequest);
        TicketRequest ticketRequest1 = new TicketRequest("2", "Ticket 2", "Description for ticket 2", Priority.MEDIUM, TicketType.STORY, TshirtSize.XL);
        Ticket ticket2 = TicketFactory.createTicket(ticketRequest1);

        ticketService.assignTicketToUser(ticket1, user1);
        ticketService.assignTicketToUser(ticket2, user1);
        ticketService.updateTicketStatus(ticket1, TicketStatus.IN_PROGRESS);
        ticketService.updateTicketStatus(ticket1, TicketStatus.CLOSED);

        commentService.addComment(ticket1, new Comment("1", "today", user2, "This is a comment for ticket 1"));
        commentService.addComment(ticket2, new Comment("2", "yesterday", user1, "This is a comment for ticket 1"));

        ticketService.getTicketsForUser(user1).
                forEach(ticket -> System.out.println("User 1 Ticket: " + ticket.getName() + " - " + ticket.getTicketStatus()));
        ticketService.getTicketsForUser(user2).
                forEach(ticket -> System.out.println("User 2 Ticket: " + ticket.getName() + " - " + ticket.getTicketStatus()));
    }
}

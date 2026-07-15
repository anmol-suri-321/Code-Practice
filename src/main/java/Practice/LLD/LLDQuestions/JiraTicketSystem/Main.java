package Practice.LLD.LLDQuestions.JiraTicketSystem;

import Practice.LLD.LLDQuestions.JiraTicketSystem.Notifications.EmailNotificationSender;
import Practice.LLD.LLDQuestions.JiraTicketSystem.Notifications.NotificationService;
import Practice.LLD.LLDQuestions.JiraTicketSystem.Notifications.WhatsappNotificationSender;
import Practice.LLD.LLDQuestions.JiraTicketSystem.Ticket.*;
import Practice.LLD.LLDQuestions.JiraTicketSystem.cache.IdempotencyStore;
import Practice.LLD.LLDQuestions.JiraTicketSystem.repository.InMemoryTicketRepository;
import Practice.LLD.LLDQuestions.JiraTicketSystem.repository.TicketRepository;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        // Create Notifications Service
        NotificationService notificationService = new NotificationService();
        IdempotencyStore<Ticket> ticketIdempotencyStore = new IdempotencyStore<>();

        TicketRepository ticketRepository = new InMemoryTicketRepository();

        TicketService ticketService = new TicketService(notificationService, ticketIdempotencyStore, ticketRepository);
        notificationService.addNotificationSender(new EmailNotificationSender());
        notificationService.addNotificationSender(new WhatsappNotificationSender());

        testInMemoryTicketRepository(ticketService);

//        testIdempotencyForCreateTicket(ticketService);
        testIdempotencyForAssignTicket(ticketService);
//        CommentService commentService = new CommentService(notificationService);
//

//
//        User user1 = new User("1", "Anmol Suri", "anmolsuri1@gmail.com", "12345");
//        User user2 = new User("2", "Anmol Suri 2", "asuri2@gmail.com", "23456");
//
//        TicketRequest ticketRequest = new TicketRequest("1", "Ticket 1", "Description for ticket 1", Priority.HIGH, TicketType.BUG, BugSeverity.CRITICAL);
//        Ticket ticket1 = TicketFactory.createTicket(ticketRequest);
//        TicketRequest ticketRequest1 = new TicketRequest("2", "Ticket 2", "Description for ticket 2", Priority.MEDIUM, TicketType.STORY, TshirtSize.XL);
//        Ticket ticket2 = TicketFactory.createTicket(ticketRequest1);
//
//        ticketService.assignTicketToUser(ticket1, user1);
//        ticketService.assignTicketToUser(ticket2, user1);
//        ticketService.updateTicketStatus(ticket1, TicketStatus.IN_PROGRESS);
//        ticketService.updateTicketStatus(ticket1, TicketStatus.CLOSED);
//        try {
//            testConcurrencyOnAssignTicket(ticketService, user1, user2);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        commentService.addComment(ticket1, new Comment("1", "today", user2, "This is a comment for ticket 1"));
//        commentService.addComment(ticket2, new Comment("2", "yesterday", user1, "This is a comment for ticket 1"));
//
//        ticketService.getTicketsForUser(user1).
//                forEach(ticket -> System.out.println("User 1 Ticket: " + ticket.getName() + " - " + ticket.getTicketStatus()));
//        ticketService.getTicketsForUser(user2).
//                forEach(ticket -> System.out.println("User 2 Ticket: " + ticket.getName() + " - " + ticket.getTicketStatus()));
    }

    private static void testInMemoryTicketRepository(TicketService ticketService) {
        TicketRequest ticketRequest = new TicketRequest("1", "Login crash", "NPE on login", Priority.HIGH, TicketType.BUG, BugSeverity.CRITICAL);
        Ticket ticket = ticketService.createTicket(ticketRequest, "repo-test-key-1");

        Optional<Ticket> retrievedTicket = ticketService.findById("1");
        Ticket t = retrievedTicket.orElseThrow(() -> new RuntimeException("Ticket not found"));
        System.out.println("Retrieved Ticket: " + t.getName() + " - " + t.getDescription());
    }

    private static void testIdempotencyForAssignTicket(TicketService ticketService) {
        String idempotencyKey = "create-ticket-user1";

        Ticket ticket = TicketFactory.createTicket(
                new TicketRequest("1", "Login crash", "NPE on login", Priority.HIGH, TicketType.BUG, BugSeverity.CRITICAL)
        );
        User user = new User("1", "Anmol Suri", "anmolsuri@gmail.com", "1234");

        ticketService.assignTicketToUser(ticket, user, idempotencyKey);
        System.out.println("First assignment — ticket assigned to: " + ticket.getUser().getName());

        ticketService.assignTicketToUser(ticket, user, idempotencyKey);
        System.out.println("Second assignment — ticket assigned to: " + ticket.getUser().getName());
    }

    private static void testIdempotencyForCreateTicket(TicketService ticketService) {
        String idempotencyKey = "create-ticket-abc123";

        Ticket t1 = ticketService.createTicket(
                new TicketRequest("1", "Login crash", "NPE on login", Priority.HIGH, TicketType.BUG, BugSeverity.CRITICAL),
                idempotencyKey
        );
        System.out.println("First call — ticket id: " + t1.getId());

        // simulate retry with same key
        Ticket t2 = ticketService.createTicket(
                new TicketRequest("1", "Login crash", "NPE on login", Priority.HIGH, TicketType.BUG, BugSeverity.CRITICAL),
                idempotencyKey
        );
        System.out.println("Second call — ticket id: " + t2.getId());

        System.out.println("Same ticket? " + (t1 == t2));
    }

//    static void testConcurrencyOnAssignTicket(TicketService ticketService, User user1, User user2) throws InterruptedException {
//        // Simulate concurrent modification
//        Ticket ticket1 = TicketFactory.createTicket(
//                new TicketRequest("1", "Login crash", "NPE on login", Priority.HIGH, TicketType.BUG, BugSeverity.CRITICAL)
//        );
//
//        Thread t1 = new Thread(() -> {
//            try {
//                ticketService.assignTicketToUser(ticket1, user1);
//                System.out.println("T1: assigned successfully, version: " + ticket1.getVersion());
//            } catch (RuntimeException e) {
//                System.out.println("T1: conflict detected — " + e.getMessage());
//            }
//        });
//
//        Thread t2 = new Thread(() -> {
//            try {
//                ticketService.assignTicketToUser(ticket1, user2);
//                System.out.println("T2: assigned successfully, version: " + ticket1.getVersion());
//            } catch (RuntimeException e) {
//                System.out.println("T2: conflict detected — " + e.getMessage());
//            }
//        });
//
//        t1.start();
//        t2.start();
//        t1.join();
//        t2.join();
//
//        System.out.println("Final assignee: " + ticket1.getUser().getName());
//        System.out.println("Final version: " + ticket1.getVersion());
//    }
}

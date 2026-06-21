# Jira Ticket System — LLD

## What This Is
A Low Level Design of a simplified Jira-like ticket management system with a pluggable notification service built using the Observer design pattern.

---

## Package Structure

```
JiraTicketSystem/
├── Ticket.java
├── TicketStatus.java
├── TicketService.java
├── Priority.java
├── User.java
├── Project.java
├── Notifications/
│   ├── NotificationEventType.java
│   ├── NotificationEvent.java
│   ├── NotificationSender.java          ← interface
│   ├── NotificationService.java
│   ├── EmailNotificationSender.java
│   └── WhatsappNotificationSender.java
└── Main.java
```

---

## UML Class Diagram

```
┌─────────────────────┐          ┌──────────────────────┐
│      <<enum>>       │          │       <<enum>>        │
│    TicketStatus     │          │       Priority        │
│─────────────────────│          │──────────────────────│
│  OPEN               │          │  LOW                  │
│  IN_PROGRESS        │          │  MEDIUM               │
│  CLOSED             │          │  HIGH                 │
└─────────────────────┘          └──────────────────────┘
           │                                │
           │ uses                           │ uses
           ▼                                ▼
┌──────────────────────────────────────────────────────┐
│                        Ticket                        │
│──────────────────────────────────────────────────────│
│  - id: String                                        │
│  - name: String                                      │
│  - description: String                               │
│  - ticketStatus: TicketStatus                        │
│  - priority: Priority                                │
│  - user: User                                        │
│──────────────────────────────────────────────────────│
│  + assignTicket(user: User): void                    │
│  + getUser(): User                                   │
│  + getTicketStatus(): TicketStatus                   │
│  + setTicketStatus(status: TicketStatus): void       │
└──────────────────────────────────────────────────────┘
           │ assigned to
           ▼
┌──────────────────────────────────────────────────────┐
│                         User                         │
│──────────────────────────────────────────────────────│
│  - id: String                                        │
│  - name: String                                      │
│  - email: String                                     │
│  - phoneNumber: String                               │
│  - assignedTickets: List<Ticket>                     │
│──────────────────────────────────────────────────────│
│  + assignTicket(ticket: Ticket): void                │
│  + getAssignedTickets(): List<Ticket>                │
└──────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────┐
│                    TicketService                     │
│──────────────────────────────────────────────────────│
│  - notificationService: NotificationService          │
│──────────────────────────────────────────────────────│
│  + assignTicketToUser(ticket, user): void            │
│  + updateTicketStatus(ticket, status): void          │
│  + getTicketsForUser(user): List<Ticket>             │
└──────────────────────────────────────────────────────┘
           │ fires events via
           ▼
┌──────────────────────────────────────────────────────┐
│                  NotificationService                 │
│──────────────────────────────────────────────────────│
│  - senders: List<NotificationSender>                 │
│──────────────────────────────────────────────────────│
│  + addNotificationSender(sender): void               │
│  + notify(event: NotificationEvent): void            │
└──────────────────────────────────────────────────────┘
           │ loops and calls send()
           ▼
┌──────────────────────────────────────────────────────┐
│               <<interface>>                          │
│             NotificationSender                       │
│──────────────────────────────────────────────────────│
│  + send(event: NotificationEvent): void              │
└──────────────────────────────────────────────────────┘
           △                    △
           │ implements         │ implements
           │                    │
┌──────────────────┐   ┌────────────────────────────┐
│  EmailNotifi-    │   │  WhatsappNotification-      │
│  cationSender    │   │  Sender                     │
│──────────────────│   │────────────────────────────│
│ send(event)      │   │ send(event)                 │
│ → email user     │   │ → whatsapp user             │
└──────────────────┘   └────────────────────────────┘

┌──────────────────────────────────────────────────────┐
│                  NotificationEvent                   │
│──────────────────────────────────────────────────────│
│  - eventType: NotificationEventType                  │
│  - ticket: Ticket                                    │
│  - user: User                                        │
│  - message: String                                   │
└──────────────────────────────────────────────────────┘

┌─────────────────────────┐
│        <<enum>>         │
│   NotificationEventType │
│─────────────────────────│
│  TICKET_ASSIGNED        │
│  STATUS_CHANGED         │
│  PRIORITY_CHANGED       │
└─────────────────────────┘
```

---

## How It Works — Step by Step Flow

```
Main.java
  │
  ├── Creates NotificationService
  ├── Adds EmailNotificationSender
  ├── Adds WhatsappNotificationSender
  │
  ├── Creates TicketService(notificationService)   ← Dependency Injection
  │
  ├── assignTicketToUser(ticket, user)
  │     │
  │     ├── ticket.assignTicket(user)
  │     ├── user.assignTicket(ticket)
  │     └── notificationService.notify(
  │               NotificationEvent(TICKET_ASSIGNED, ticket, user, msg))
  │                     │
  │                     ├── EmailNotificationSender.send(event)
  │                     └── WhatsappNotificationSender.send(event)
  │
  └── updateTicketStatus(ticket, newStatus)
        │
        ├── validates OPEN → CLOSED not allowed
        ├── ticket.setTicketStatus(newStatus)
        └── notificationService.notify(
                  NotificationEvent(STATUS_CHANGED, ticket, ticket.getUser(), msg))
                        │
                        ├── EmailNotificationSender.send(event)
                        └── WhatsappNotificationSender.send(event)
```

---

## Design Pattern Used

### Observer Pattern
| Observer Concept | This System |
|---|---|
| Subject / Observable | `TicketService` (fires events) |
| Observer interface | `NotificationSender` |
| Concrete Observer | `EmailNotificationSender`, `WhatsappNotificationSender` |
| Notify method | `NotificationService.notify(event)` |
| Event data | `NotificationEvent` |

`TicketService` does not know whether notification goes via Email or WhatsApp. It just calls `notify()`. `NotificationService` handles delivery. Adding a new channel (Slack, SMS) = implement `NotificationSender`, call `addNotificationSender()`. Zero changes to existing code.

---

## SOLID Principles Applied

### S — Single Responsibility Principle
Each class has one job:
- `Ticket` → holds ticket data
- `TicketService` → manages ticket operations
- `NotificationService` → orchestrates notifications
- `EmailNotificationSender` → only sends emails

### O — Open/Closed Principle
`NotificationService` is open for extension, closed for modification.
Adding `SlackNotificationSender` requires zero changes to existing classes — just implement `NotificationSender` and register it.

### L — Liskov Substitution Principle
`EmailNotificationSender` and `WhatsappNotificationSender` are interchangeable wherever `NotificationSender` is expected. `List<NotificationSender>` holds both without distinction.

### I — Interface Segregation Principle
`NotificationSender` has exactly one method — `send(event)`. No bloated interface forcing implementors to stub unused methods.

### D — Dependency Inversion Principle
`TicketService` depends on `NotificationService` abstraction injected from outside — not on `EmailNotificationSender` or `WhatsappNotificationSender` concretions.
`NotificationService` depends on `NotificationSender` interface — not concrete senders.

---

## Sample Output

```
Sending email to anmolsuri1@gmail.com with message: Ticket assigned to user: Anmol Suri
Sending WhatsApp message to 12345 with message: Ticket assigned to user: Anmol Suri
Sending email to asuri2@gmail.com with message: Ticket assigned to user: Anmol Suri 2
Sending WhatsApp message to 23456 with message: Ticket assigned to user: Anmol Suri 2
Sending email to anmolsuri1@gmail.com with message: Ticket status updated to: IN_PROGRESS
Sending WhatsApp message to 12345 with message: Ticket status updated to: IN_PROGRESS
Sending email to anmolsuri1@gmail.com with message: Ticket status updated to: CLOSED
Sending WhatsApp message to 12345 with message: Ticket status updated to: CLOSED
User 1 Ticket: Ticket 1 - CLOSED
User 1 Ticket: Ticket 2 - OPEN
User 2 Ticket: Ticket 3 - OPEN
```

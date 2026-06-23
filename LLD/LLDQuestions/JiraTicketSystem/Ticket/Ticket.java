package LLD.LLDQuestions.JiraTicketSystem.Ticket;

import LLD.LLDQuestions.JiraTicketSystem.Comment.Comment;
import LLD.LLDQuestions.JiraTicketSystem.Priority;
import LLD.LLDQuestions.JiraTicketSystem.User;

import java.util.ArrayList;
import java.util.List;

public abstract class Ticket {
    String id;
    String name;
    String description;
    TicketStatus ticketStatus;
    Priority priority;
    User user;
    List<Comment> comments;
    TicketState currentState;

    protected Ticket(String id, String name, String description, TicketStatus ticketStatus, Priority priority) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ticketStatus = ticketStatus;
        this.priority = priority;
        this.comments = new ArrayList<>();
        this.currentState = new OpenTicketState();
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void assignTicket(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void setCurrentState(TicketStatus status) {
        switch(status) {
            case OPEN -> this.currentState = new OpenTicketState();
            case IN_PROGRESS -> this.currentState = new InProgressTicketState();
            case CLOSED -> this.currentState = new ClosedTicketState();
        }
    }

    public abstract TicketType getTicketType();
}

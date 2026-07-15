package Practice.LLD.LLDQuestions.JiraTicketSystem.Ticket;

import Practice.LLD.LLDQuestions.JiraTicketSystem.Comment.Comment;
import Practice.LLD.LLDQuestions.JiraTicketSystem.Priority;
import Practice.LLD.LLDQuestions.JiraTicketSystem.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Ticket {
    private String id;
    private String name;
    private String description;
    private TicketStatus ticketStatus;
    private Priority priority;
    private User user;
    private List<Comment> comments;
    private final AtomicInteger version = new AtomicInteger(0);

    protected Ticket(String id, String name, String description, TicketStatus ticketStatus, Priority priority) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ticketStatus = ticketStatus;
        this.priority = priority;
        this.comments = new ArrayList<>();
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

    public int getVersion() {
        return version.get();
    }

    public abstract TicketType getTicketType();

    public boolean compareAndIncrementVersion(int expectedVersion) {
        return !version.compareAndSet(expectedVersion, expectedVersion + 1);
    }
}

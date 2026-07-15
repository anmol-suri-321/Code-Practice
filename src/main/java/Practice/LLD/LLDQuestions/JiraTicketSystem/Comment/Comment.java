package Practice.LLD.LLDQuestions.JiraTicketSystem.Comment;

import Practice.LLD.LLDQuestions.JiraTicketSystem.User;

public class Comment {
    String id;
    String timestamp;
    User author;
    String message;

    public Comment(String id, String timestamp, User author, String message) {
        this.id = id;
        this.timestamp = timestamp;
        this.author = author;
        this.message = message;
    }


    public String getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public User getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }
}

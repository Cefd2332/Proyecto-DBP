package org.e2e.e2e.Email;

public class EmailEvent {

    private final String to;
    private final String subject;
    private final String text;

    public EmailEvent(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "EmailEvent{" +
                "to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

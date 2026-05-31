package dto;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Email {
    private String id;
    private User sender;
    private String body;
    private List<Attachment> attachment;
    private List<User> recipients;
    private String subject;

    @Override
    public String toString() {
        return String.format("id: %s\r\nsender: %s\r\nsubject: %s\r\nbody: %s\r\nrecipients: %s", id, sender.getEmail(), subject, body, String.join(", ", recipients.stream().map(u -> u.getEmail()).toList()));
    }
}

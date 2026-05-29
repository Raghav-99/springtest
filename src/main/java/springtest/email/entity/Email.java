package springtest.email.entity;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;

@Getter
@Setter
@Entity
public class Email {
	@Id
    @GeneratedValue
	private String id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
	private User sender;
	private String body;
    @OneToMany
    @JoinColumn(name = "attachment_id")
	private List<Attachment> attachment;
    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "recipient_id"), inverseJoinColumns = @JoinColumn(name = "email_id"))
	private List<User> recipients;
	private String subject;
	
	@Override
	public String toString() {
		return String.format("id: %s\r\nsender: %s\r\nsubject: %s\r\nbody: %s\r\nrecipients: %s", id, sender.getEmail(), subject, body, String.join(", ", recipients.stream().map(u -> u.getEmail()).toList()));
	}
}

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
	private List<User> recipients;
	private String subject;
}

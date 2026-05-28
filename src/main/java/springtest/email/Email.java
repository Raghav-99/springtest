package springtest.email;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@Entity
public class Email {
	@Id
	private final String id;
	@OneToOne
	private final User sender;
	@Setter
	private String body;
	@Setter
	@OneToOne
	private Attachment attachment;
	@OneTo
	private final List<User> receiver;
	@Setter
	private String subject;
}

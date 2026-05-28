package springtest.email;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import springtest.email.EmailController.EmailResponse;


@RestController
public class EmailController implements IEmailController {

	public record EmailResponse(Email email, Exception exception) {
		
	}

	@Override
	public ResponseEntity<EmailResponse> sendMail(Email request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<EmailResponse> receiveMail(String user) {
		// TODO Auto-generated method stub
		return null;
	}
		
	interface IEmailController {
		ResponseEntity<EmailResponse> sendMail(Email request);
		ResponseEntity<EmailResponse> receiveMail(String user);
	}
}

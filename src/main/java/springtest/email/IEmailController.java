package springtest.email;

import org.springframework.http.ResponseEntity;
import springtest.email.entity.Email;

import java.util.List;

public interface IEmailController {
    ResponseEntity<EmailResponse> sendMail(EmailRequest request);

    ResponseEntity<EmailResponse> receiveMail(int user);
    record EmailResponse(Email email, Exception exception) {

    }

    record EmailRequest(String sender, List<String> recepients, String subject, String body, byte[] attachment) {}
}

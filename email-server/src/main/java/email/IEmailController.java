package email;

import dto.EmailResponse;
import dto.EmailRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IEmailController {
    ResponseEntity<EmailResponse> sendMail(EmailRequest request);

    ResponseEntity<EmailResponse> receiveMail(String user);    
}

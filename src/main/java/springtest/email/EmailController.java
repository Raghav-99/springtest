package springtest.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springtest.email.entity.Attachment;
import springtest.email.entity.Email;
import springtest.email.entity.User;
import springtest.email.service.AttachmentService;
import springtest.email.service.EmailService;
import springtest.email.service.UserService;


@RestController
@RequestMapping("/api/email")
@Profile("gmail-poc")
public class EmailController implements IEmailController {
    private final EmailService emailService;
    private final UserService userService;
    private final AttachmentService attachmentService;
    @Autowired
    public EmailController(EmailService emailService, UserService userService, AttachmentService attachmentService) {
        this.emailService = emailService;
        this.userService = userService;
        this.attachmentService = attachmentService;
    }

	@Override
    @PostMapping("/send")
	public ResponseEntity<IEmailController.EmailResponse> sendMail(EmailRequest request) {
        Email email = emailService.sendMail(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new EmailResponse(email, null));
	}

	@Override
    @PostMapping("/receive")
	public ResponseEntity<IEmailController.EmailResponse> receiveMail(int user) {
		Email email = emailService.receiveMail(user);
        return ResponseEntity.status(HttpStatus.OK).body(new EmailResponse(email, null));
	}
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<IEmailController.EmailResponse> replyOnError(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new EmailResponse(null, exception));
    }
}

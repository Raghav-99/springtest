package email;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import email.service.EmailService;
import dto.EmailResponse;
import dto.EmailRequest;
import email.mapper.EntityDtoMapper;


@RestController
@RequestMapping("/api/email")
public class EmailController implements IEmailController {
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

	@Override
    @PostMapping("/send")
    public ResponseEntity<EmailResponse> sendMail(EmailRequest request) {
        email.entity.Email emailEntity = emailService.sendMail(request);
        dto.Email dto = EntityDtoMapper.toDto(emailEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(new EmailResponse(dto, null));
    }

	@Override
    @PostMapping("/receive?email={id}")
    public ResponseEntity<EmailResponse> receiveMail(@RequestParam("id") String user) {
        email.entity.Email emailEntity = emailService.receiveMail(user);
        dto.Email dto = EntityDtoMapper.toDto(emailEntity);
        return ResponseEntity.status(HttpStatus.OK).body(new EmailResponse(dto, null));
    }
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<EmailResponse> replyOnError(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new EmailResponse(null, exception));
    }
}

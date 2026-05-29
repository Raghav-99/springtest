package springtest.email.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import springtest.email.IEmailController;
import springtest.email.entity.Attachment;
import springtest.email.entity.Email;
import springtest.email.entity.User;
import springtest.email.repository.EmailRepository;
import springtest.email.service.AttachmentService;
import springtest.email.service.EmailService;
import springtest.email.service.UserService;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {
    private final EmailRepository emailRepository;
    private final UserService userService;
    private final AttachmentService attachmentService;
    public EmailServiceImpl(EmailRepository emailRepository, UserService userService, AttachmentService attachmentService) {
        this.emailRepository = emailRepository;
        this.userService = userService;
        this.attachmentService = attachmentService;
    }
    @Override
    @Transactional
    public Email sendMail(IEmailController.EmailRequest emailRequest) {
        User user = new User();
        user.setEmail(emailRequest.sender());
        user = userService.getOrCreateUser(user);

        Attachment attachment = null;
        String blob = emailRequest.attachment();
        if(blob != null && !blob.isBlank()) {
            attachment = new Attachment();
            attachment.setBlob(Base64.getDecoder().decode(blob));
            attachment = attachmentService.saveAttachment(attachment);
        }

        List<User> recepients = emailRequest.recepients().stream().map((sender) -> {
            User recepient = new User();
            recepient.setEmail(sender);
            recepient = userService.getOrCreateUser(recepient);
            return recepient;
        }).toList();
        return emailRepository.save(setEmail(emailRequest.body(), emailRequest.subject(), user, recepients, attachment));
    }

    private Email setEmail(String body, String subject, User user, List<User> recepients, Attachment attachment) {
        Email email = new Email();
        email.setBody(body);
        email.setSubject(subject);
        email.setSender(user);
        email.setRecipients(recepients);
        email.setAttachment(List.of(attachment));
        return email;
    }

    @Override
    public Email receiveMail(int user) {
        return emailRepository.findByRecepientId(user);
    }
}

package email.service;

import email.entity.Email;
import dto.EmailRequest;

public interface EmailService {
    Email sendMail(EmailRequest emailRequest);
    Email receiveMail(String user);
}

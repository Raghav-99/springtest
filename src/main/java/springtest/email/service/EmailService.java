package springtest.email.service;

import springtest.email.IEmailController;
import springtest.email.entity.Email;

public interface EmailService {
    Email sendMail(IEmailController.EmailRequest emailRequest);
    Email receiveMail(int user);
}

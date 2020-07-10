package com.idetix.identityapprover.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.EmailValidator;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Profile("default")
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public boolean sendSecretViaEmail(String to, String secret) {
        boolean result = false;
        boolean allowLocal = true;
        boolean valid = EmailValidator.getInstance(allowLocal).isValid(to);
        if (!valid){
            return result;
        }
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
            String htmlMsg = "<body style='border:2px solid black'>"
                    + "Your secret token for registration is  <br>"
                    + secret
                    + "<br>Please use this OTP to complete your new user registration." +
                    "OTP is confidential, do not share this  with anyone.</body>";
            message.setContent(htmlMsg, "text/html");
            helper.setTo(to);
            helper.setSubject("Your Verification Token");
            emailSender.send(message);
            result = true;
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
        return result;
    }

}

package com.idetix.identityapprover.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Profile("!default")
@Service
public class EmailServiceMockImpl implements EmailService {

    public boolean sendSecretViaEmail(String to, String secret) {
        System.out.println("Sent to "+to+"; Secret Token: "+secret);
        return true;
    }


}

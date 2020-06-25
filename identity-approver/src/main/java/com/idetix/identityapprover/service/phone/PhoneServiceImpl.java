package com.idetix.identityapprover.service.phone;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class PhoneServiceImpl implements PhoneService {
    @Autowired
    private JavaMailSender emailSender;

    public static final String ACCOUNT_SID = "AC70cb2d321fb7eef1ae3c08d142151b97";
    public static final String AUTH_TOKEN = "9987820d5e10fc0a5fe30857577b3ad2";

    public boolean sendSecretViaSMS(String to, String secret) {
        // Find your Account Sid and Token at twilio.com/user/account

            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            Message message = Message.creator(new PhoneNumber(to),
                    new PhoneNumber("+1 530 615 0664"),
                    "Your secret Token is: "+ secret).create();
            return true;
    }


}

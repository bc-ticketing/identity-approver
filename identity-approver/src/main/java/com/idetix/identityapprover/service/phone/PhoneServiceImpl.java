package com.idetix.identityapprover.service.phone;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.twilio.Twilio.init;

@Service
public class PhoneServiceImpl implements PhoneService {

    private String myTwilioPhoneNumber;

    @Autowired
    public PhoneServiceImpl(
            @Value("${twilioAccountSid}") String twilioAccountSid,
            @Value("${twilioAuthToken}") String twilioAuthToken,
            @Value("${twiliophoneNumber}") String twilioPhoneNumber) {
        myTwilioPhoneNumber = twilioPhoneNumber;
        init(twilioAccountSid, twilioAuthToken);
    }

    public boolean sendSecretViaSMS(String to, String secret) {
            Message message = Message.creator(new PhoneNumber(to),
                    new PhoneNumber(myTwilioPhoneNumber),
                    "Your secret Token is: "+ secret).create();
            return true;
    }

}

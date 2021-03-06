package com.idetix.identityapprover.service.phone;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static com.twilio.Twilio.init;

@Profile("!dev")
@Service
public class PhoneServiceImpl implements PhoneService {

    private final String myTwilioPhoneNumber;

    @Autowired
    public PhoneServiceImpl(
            @Value("${twilioAccountSid}") String twilioAccountSid,
            @Value("${twilioAuthToken}") String twilioAuthToken,
            @Value("${twiliophoneNumber}") String twilioPhoneNumber) {
        myTwilioPhoneNumber = twilioPhoneNumber;
        init(twilioAccountSid, twilioAuthToken);
    }

    public boolean sendSecretViaSMS(String to, String secret) {
        try {
            Message message = Message.creator(new PhoneNumber(to),
                    new PhoneNumber(myTwilioPhoneNumber),
                    "Your secret Token is: " + secret).create();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

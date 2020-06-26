package com.idetix.identityapprover.service.phone;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static com.twilio.Twilio.init;

@Profile("!default")
@Service
public class PhoneServiceMockImpl implements PhoneService {

    public boolean sendSecretViaSMS(String to, String secret) {
        System.out.println("Sent to "+to+"; Secret Token: "+secret);
            return true;
    }

}

package com.idetix.identityapprover.service.email;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("!default")
@Service
public class EmailServiceMockImpl implements EmailService {

    public boolean sendSecretViaEmail(String to, String secret) {
        System.out.println("Sent to " + to + "; Secret Token: " + secret);
        return true;
    }

}

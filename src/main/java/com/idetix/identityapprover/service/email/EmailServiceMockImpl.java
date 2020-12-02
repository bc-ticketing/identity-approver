package com.idetix.identityapprover.service.email;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("dev")
@Service
public class EmailServiceMockImpl implements EmailService {

    public boolean sendSecretViaEmail(String to, String secret) {
        boolean allowLocal = true;
        boolean valid = EmailValidator.getInstance(true).isValid(to);
        if (!valid){
            return false;
        }
        System.out.println("Sent to " + to + "; Secret Token: " + secret);
        return true;
    }

}

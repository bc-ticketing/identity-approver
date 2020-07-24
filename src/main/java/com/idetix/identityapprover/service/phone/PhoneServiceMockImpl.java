package com.idetix.identityapprover.service.phone;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("dev")
@Service
public class PhoneServiceMockImpl implements PhoneService {

    public boolean sendSecretViaSMS(String to, String secret) {
        System.out.println("Sent to " + to + "; Secret Token: " + secret);
        return true;
    }

}

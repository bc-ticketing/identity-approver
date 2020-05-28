package com.idetix.identityapprover.service;


import com.idetix.identityapprover.entity.EmailIdentity;
import com.idetix.identityapprover.repository.EmailIdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailIdentityService {

    @Autowired
    private EmailIdentityRepository repository;

    public EmailIdentity saveEmailIdentity(EmailIdentity emailIdentity){
        if (repository.findById(emailIdentity.getEmail()).orElse(null) == null){
            return repository.save(emailIdentity);
        }
        return null;
    }

    public EmailIdentity getEmailIdentityById(String email){
        return repository.findById(email).orElse(null);
    }

    public EmailIdentity updateEmailIdentity(EmailIdentity emailIdentity){
        EmailIdentity existingEmailIdentity = repository.findById(emailIdentity.getEmail()).orElse(null);
        existingEmailIdentity.setEthAddress(emailIdentity.getEthAddress());
        existingEmailIdentity.setSecret(emailIdentity.getSecret());
        existingEmailIdentity.setVerified(emailIdentity.getVerified());
        return repository.save(existingEmailIdentity);
    }
}

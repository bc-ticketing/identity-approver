package com.idetix.identityapprover.service;


import com.idetix.identityapprover.entity.EmailIdentity;
import com.idetix.identityapprover.repository.EmailIdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailIdentityService {

    @Autowired
    private EmailIdentityRepository repository;

    public boolean addEmailIdentity(String eMail){
        if (repository.findById(eMail).orElse(null)== null){
            EmailIdentity emailIdentity = new EmailIdentity(eMail,generateSecret(),"",false);
            repository.save(emailIdentity);
            // TODO: 19.06.2020  : send Email
            return true;
        }
        EmailIdentity emailIdentity = getEmailIdentityById(eMail);
        emailIdentity.setSecret(generateSecret());
        updateEmailIdentity(emailIdentity);
        // TODO: 19.06.2020 : Send Email again
        return true;
    }

    public EmailIdentity approveEmailIdentity(String eMail, String ethAddress, String secret, String signedSecret){
        if (repository.findById(eMail).orElse(null)== null){
            return null;
        }
        EmailIdentity emailIdentity = getEmailIdentityById(eMail);
        if (emailIdentity.getSecret() == secret &&
                SecurityService.verifyAddressFromSignature(ethAddress,signedSecret,secret)){
            // TODO: 19.06.2020 : Call Smart Contract and Verify Address
            emailIdentity.setVerified(true);
            emailIdentity.setEthAddress(ethAddress);
        }

        return emailIdentity;
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
    private String generateSecret(){
        return SecurityService.getAlphaNumericString(42);
    }


}


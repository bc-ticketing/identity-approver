package com.idetix.identityapprover.service.email;

import com.idetix.identityapprover.entity.EmailIdentity;
import com.idetix.identityapprover.repository.EmailIdentityRepository;
import com.idetix.identityapprover.service.blockchain.BlockchainService;
import com.idetix.identityapprover.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailIdentityService {
    @Autowired
    private EmailIdentityRepository repository;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BlockchainService blockchainService;

    // Method to creat a new Request to verify a new eMail Address
    // If the Address already exists, a new secret is Sent
    // If the Address is allready verified return false
    public boolean addEmailIdentity(String eMail) {
        if (repository.findById(eMail).orElse(null) == null) {
            EmailIdentity emailIdentity = new EmailIdentity(eMail, generateSecret(), "", false);
            if (emailService.sendSecretViaEmail(emailIdentity.getEmail(), emailIdentity.getSecret()) == true) {
                repository.save(emailIdentity);
            } else {
                return false;
            }
            return true;
        }
        EmailIdentity emailIdentity = getEmailIdentityById(eMail);
        if (emailIdentity.getVerified() == true) {
            return false;
        }
        emailIdentity.setSecret(generateSecret());
        if (emailService.sendSecretViaEmail(emailIdentity.getEmail(), emailIdentity.getSecret()) == true) {
            updateEmailIdentity(emailIdentity);
        } else {
            return false;
        }
        return true;
    }

    //Method to finaly verify the eMail Address and make it unchangable in the Database
    // When the provided secret corrospond with the secret on the DB and the provided Signature is correct for the secret
    // and ETHAddress, the Identity gets verified on the Blockchain and is set to verified on the DB
    public EmailIdentity verifyEmailIdentity(String eMail, String ethAddress, String secret, String signedSecret) {
        if (getEmailIdentityById(eMail) == null) {
            return null;
        }
        EmailIdentity emailIdentity = getEmailIdentityById(eMail);
        if (emailIdentity.getSecret().contentEquals(secret) &&
                securityService.verifyAddressFromSignature(ethAddress, signedSecret, secret)) {
            if (blockchainService.getSecurityLevelforAdress(ethAddress) < 1) {
                if (blockchainService.saveIdentityProofToChain(ethAddress, 1) == true) {
                    emailIdentity.setVerified(true);
                    emailIdentity.setEthAddress(ethAddress);
                    updateEmailIdentity(emailIdentity);
                }
            }
        }

        return emailIdentity;
    }

    private EmailIdentity getEmailIdentityById(String email) {
        return repository.findById(email).orElse(null);
    }

    private EmailIdentity updateEmailIdentity(EmailIdentity emailIdentity) {
        EmailIdentity existingEmailIdentity = repository.findById(emailIdentity.getEmail()).orElse(null);
        existingEmailIdentity.setEthAddress(emailIdentity.getEthAddress());
        existingEmailIdentity.setSecret(emailIdentity.getSecret());
        existingEmailIdentity.setVerified(emailIdentity.getVerified());
        return repository.save(existingEmailIdentity);
    }

    private String generateSecret() {
        return securityService.getAlphaNumericString(42, false);
    }

}


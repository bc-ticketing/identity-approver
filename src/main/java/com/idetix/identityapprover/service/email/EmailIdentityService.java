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
            if (emailService.sendSecretViaEmail(emailIdentity.getEmail(), emailIdentity.getSecret())) {
                repository.save(emailIdentity);
            } else {
                return false;
            }
            return true;
        }
        EmailIdentity emailIdentity = getEmailIdentityById(eMail);
        if (emailIdentity.getVerified()) {
            return false;
        }
        emailIdentity.setSecret(generateSecret());
        if (emailService.sendSecretViaEmail(emailIdentity.getEmail(), emailIdentity.getSecret())) {
            updateEmailIdentity(emailIdentity);
        } else {
            return false;
        }
        return true;
    }

    // Method to verify the EMail address and make it final in the database.
    // When the provided secret corresponds with the secret on the database, the provided signature is correct
    // for the secret and ETH address, the identity gets verified on the Blockchain and is set to verified on the database.
    public EmailIdentity verifyEmailIdentity(String eMail, String ethAddress, String secret, String signedSecret) {
        EmailIdentity emailIdentity = getEmailIdentityById(eMail);
        if (emailIdentity.getSecret().contentEquals(secret) &&
                securityService.verifyAddressFromSignature(ethAddress, signedSecret, secret)) {
            if (blockchainService.getSecurityLevelForAddress(ethAddress) < 1) {
                if (blockchainService.saveIdentityProofToChain(ethAddress, 1)) {
                    emailIdentity.setVerified(true);
                    emailIdentity.setEthAddress(ethAddress);
                    updateEmailIdentity(emailIdentity);
                }
            }
        }

        return emailIdentity;
    }

    private EmailIdentity getEmailIdentityById(String email) {
        EmailIdentity emailIdentity = repository.findById(email).orElse(null);
        if (emailIdentity == null) {
            throw new IllegalArgumentException("The repository does not contain an EMail identity for the EMail" +
                    " `" + email + "`.");
        }
        return emailIdentity;
    }

    private EmailIdentity updateEmailIdentity(EmailIdentity emailIdentity) {
        EmailIdentity existingEmailIdentity = repository.findById(emailIdentity.getEmail()).orElse(null);
        if (existingEmailIdentity == null) {
            throw new IllegalArgumentException("The repository does not contain an EMail identity for the EmailIdentity" +
                    " `" + emailIdentity + "`.");
        }
        existingEmailIdentity.setEthAddress(emailIdentity.getEthAddress());
        existingEmailIdentity.setSecret(emailIdentity.getSecret());
        existingEmailIdentity.setVerified(emailIdentity.getVerified());
        return repository.save(existingEmailIdentity);
    }

    private String generateSecret() {
        return securityService.getAlphaNumericString(42, false);
    }

}


package com.idetix.identityapprover.service.phone;

import com.idetix.identityapprover.entity.PhoneIdentity;
import com.idetix.identityapprover.repository.PhoneIdentityRepository;
import com.idetix.identityapprover.service.blockchain.BlockchainService;
import com.idetix.identityapprover.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneIdentityService {
    @Autowired
    private PhoneIdentityRepository repository;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private PhoneService phoneService;
    @Autowired
    private BlockchainService blockchainService;

    // Method to creat a new Request to verify a new eMail Address
    // If the Address already exists, a new secret is Sent
    // If the Address is allready verified return false
    public boolean addPhoneIdentity(String phoneNr) {
        if (repository.findById(phoneNr).orElse(null) == null) {
            PhoneIdentity phoneIdentity = new PhoneIdentity(phoneNr, generateSecret(), "", false);
            if (phoneService.sendSecretViaSMS(phoneIdentity.getPhoneNr(), phoneIdentity.getSecret()) == true) {
                repository.save(phoneIdentity);
            } else {
                return false;
            }
            return true;
        }
        PhoneIdentity phoneIdentity = getPhoneIdentityById(phoneNr);
        if (phoneIdentity.getVerified() == true) {
            return false;
        }
        phoneIdentity.setSecret(generateSecret());
        if (phoneService.sendSecretViaSMS(phoneIdentity.getPhoneNr(), phoneIdentity.getSecret()) == true) {
            updatePhoneIdentity(phoneIdentity);
        } else {
            return false;
        }
        return true;
    }

    //Method to finaly verify the eMail Address and make it unchangable in the Database
    // When the provided secret corrospond with the secret on the DB and the provided Signature is correct for the secret
    // and ETHAddress, the Identity gets verified on the Blockchain and is set to verified on the DB
    public PhoneIdentity verifyPhoneIdentity(String phoneNr, String ethAddress, String secret, String signedSecret) {
        if (repository.findById(phoneNr).orElse(null) == null) {
            return null;
        }
        PhoneIdentity phoneIdentity = getPhoneIdentityById(phoneNr);
        if (phoneIdentity.getSecret().contentEquals(secret) &&
                securityService.verifyAddressFromSignature(ethAddress, signedSecret, secret)) {
            if (blockchainService.SaveIdentityProofToChain(ethAddress, 2) == true) {
                phoneIdentity.setVerified(true);
                phoneIdentity.setEthAddress(ethAddress);
                updatePhoneIdentity(phoneIdentity);
            }
        }

        return phoneIdentity;
    }

    private PhoneIdentity getPhoneIdentityById(String phoneNr) {
        return repository.findById(phoneNr).orElse(null);
    }

    private PhoneIdentity updatePhoneIdentity(PhoneIdentity phoneIdentity) {
        PhoneIdentity existingPhoneIdentity = repository.findById(phoneIdentity.getPhoneNr()).orElse(null);
        existingPhoneIdentity.setEthAddress(phoneIdentity.getEthAddress());
        existingPhoneIdentity.setSecret(phoneIdentity.getSecret());
        existingPhoneIdentity.setVerified(phoneIdentity.getVerified());
        return repository.save(existingPhoneIdentity);
    }

    private String generateSecret() {
        return securityService.getAlphaNumericString(4, true);
    }

}


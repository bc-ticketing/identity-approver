package com.idetix.identityapprover.service.phone;

import com.idetix.identityapprover.entity.Exceptions.BlockChainWriteFailedException;
import com.idetix.identityapprover.entity.Exceptions.IdentityNotFoundException;
import com.idetix.identityapprover.entity.Exceptions.SecretMismatchException;
import com.idetix.identityapprover.entity.Exceptions.SignatureMismatchException;
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

    // Method to create a new Request to verify a new EMail address.
    // If the address already exists, a new secret is sent.
    // If the address is already verified return false.
    public boolean addPhoneIdentity(String phoneNr) {
        if (repository.findById(phoneNr).orElse(null) == null) {
            PhoneIdentity phoneIdentity = new PhoneIdentity(phoneNr, generateSecret(), "", false);
            if (phoneService.sendSecretViaSMS(phoneIdentity.getPhoneNr(), phoneIdentity.getSecret())) {
                repository.save(phoneIdentity);
            } else {
                return false;
            }
            return true;
        }
        PhoneIdentity phoneIdentity = getPhoneIdentityById(phoneNr);
        if (phoneIdentity.getVerified()) {
            return false;
        }
        phoneIdentity.setSecret(generateSecret());
        if (phoneService.sendSecretViaSMS(phoneIdentity.getPhoneNr(), phoneIdentity.getSecret())) {
            updatePhoneIdentity(phoneIdentity);
        } else {
            return false;
        }
        return true;
    }

    // Method to verify the EMail address and make it final in the database.
    // When the provided secret corresponds with the secret on the database, the provided signature is correct
    // for the secret and ETH address, the identity gets verified on the Blockchain and is set to verified on the database.
    public PhoneIdentity verifyPhoneIdentity(String phoneNr, String ethAddress, String secret, String signedSecret) throws IdentityNotFoundException, SecretMismatchException,SignatureMismatchException, BlockChainWriteFailedException {
        PhoneIdentity phoneIdentity = getPhoneIdentityById(phoneNr);
        if (phoneIdentity == null){
            throw new IdentityNotFoundException("The Provided Identity does not exist");
        }
        if (phoneIdentity.getSecret().contentEquals(secret) &&
                securityService.verifyAddressFromSignature(ethAddress, signedSecret, secret)) {
            if (blockchainService.getSecurityLevelForAddress(ethAddress) < 2) {
                if (blockchainService.saveIdentityProofToChain(ethAddress, 2)) {
                    phoneIdentity.setVerified(true);
                    phoneIdentity.setEthAddress(ethAddress);
                    updatePhoneIdentity(phoneIdentity);
                }
                else {
                    throw new BlockChainWriteFailedException("could not write to Blockchain");
                }
            }
        }
        else if (securityService.verifyAddressFromSignature(ethAddress, signedSecret, secret)){
            throw new SignatureMismatchException("provided Secret does not correspond to Signature provided");
        }
        else if (phoneIdentity.getSecret().contentEquals(secret)){
            throw new SecretMismatchException("provided Secret does not match the sent Secret");
        }
        return phoneIdentity;
    }

    private PhoneIdentity getPhoneIdentityById(String phoneNr) {
        PhoneIdentity phoneIdentity = repository.findById(phoneNr).orElse(null);
        if (phoneIdentity == null) {
            throw new IllegalArgumentException("The repository does not contain an EMail identity for the EMail" +
                    " `" + phoneNr + "`.");
        }
        return phoneIdentity;
    }

    private void updatePhoneIdentity(PhoneIdentity phoneIdentity) {
        PhoneIdentity existingPhoneIdentity = repository.findById(phoneIdentity.getPhoneNr()).orElse(null);
        if (existingPhoneIdentity == null) {
            throw new IllegalArgumentException("The repository does not contain an EMail identity for the EmailIdentity" +
                    " `" + phoneIdentity + "`.");
        }
        existingPhoneIdentity.setEthAddress(phoneIdentity.getEthAddress());
        existingPhoneIdentity.setSecret(phoneIdentity.getSecret());
        existingPhoneIdentity.setVerified(phoneIdentity.getVerified());
        repository.save(existingPhoneIdentity);
    }

    private String generateSecret() {
        return securityService.getAlphaNumericString(4, true);
    }

}


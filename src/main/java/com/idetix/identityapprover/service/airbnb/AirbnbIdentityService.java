package com.idetix.identityapprover.service.airbnb;

import com.idetix.identityapprover.entity.AirbnbIdentity;
import com.idetix.identityapprover.repository.AirbnbIdentityRepository;
import com.idetix.identityapprover.service.blockchain.BlockchainService;
import com.idetix.identityapprover.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirbnbIdentityService {
    @Autowired
    private AirbnbIdentityRepository repository;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private BlockchainService blockchainService;

    // Method to create a new Request to verify a new profileUrl address.
    // If the address already exists, a new secret is sent.
    // If the address is already verified return false.
    public String addAirbnbIdentity(String profileUrl) {
        if (repository.findById(profileUrl).orElse(null) == null) {
            AirbnbIdentity airbnbIdentity = new AirbnbIdentity(profileUrl, generateSecret(), "", false);
            saveAirbnbIdentityById(airbnbIdentity);
            return airbnbIdentity.getSecret();
            }
        AirbnbIdentity airbnbIdentity = getAirbnbIdentityById(profileUrl);
        if (airbnbIdentity.getVerified()) {
            return "already verified";
        }
        airbnbIdentity.setSecret(generateSecret());
        updateAirbnbIdentity(airbnbIdentity);
        return airbnbIdentity.getSecret();
    }

    // Method to verify the Airbnb Profile address and make it final in the Database
    // When the provided secret corresponds with the secret on the DB and the provided Signature is correct for the secret
    // and the ETH address, the Identity gets verified on the Blockchain and its status is set to `verified` on the database.
    public AirbnbIdentity verifyAirbnbIdentity(String profileUrl, String ethAddress) {
        AirbnbIdentity airbnbIdentity = getAirbnbIdentityById(profileUrl);
        // TODO: 10.07.2020 get Signed String from Profile and check if verified
        String signedSecret = "yes";

        if (securityService.verifyAddressFromSignature(ethAddress, signedSecret, airbnbIdentity.getSecret())) {
            if (blockchainService.getSecurityLevelForAddress(ethAddress) < 3) {
                if (blockchainService.saveIdentityProofToChain(ethAddress, 3)) {
                    airbnbIdentity.setVerified(true);
                    airbnbIdentity.setEthAddress(ethAddress);
                    updateAirbnbIdentity(airbnbIdentity);
                }
            }
        }
        return airbnbIdentity;
    }

    private AirbnbIdentity getAirbnbIdentityById(String profileUrl) {
        AirbnbIdentity airbnbIdentity = repository.findById(profileUrl).orElse(null);
        if (airbnbIdentity == null) {
            throw new IllegalArgumentException("The repository does not contain an EMail identity for the EMail" +
                    " `" + profileUrl + "`.");
        }
        return airbnbIdentity;
    }

    private AirbnbIdentity saveAirbnbIdentityById(AirbnbIdentity airbnbIdentity) {
        return repository.save(airbnbIdentity);
    }

    private AirbnbIdentity updateAirbnbIdentity(AirbnbIdentity airbnbIdentity) {
        AirbnbIdentity existingAirbnbIdentity = repository.findById(airbnbIdentity.getProfileUrl()).orElse(null);
        if (existingAirbnbIdentity == null) {
            throw new IllegalArgumentException("The repository does not contain an EMail identity for the EmailIdentity" +
                    " `" + airbnbIdentity + "`.");
        }
        existingAirbnbIdentity.setEthAddress(airbnbIdentity.getEthAddress());
        existingAirbnbIdentity.setSecret(airbnbIdentity.getSecret());
        existingAirbnbIdentity.setVerified(airbnbIdentity.getVerified());
        return repository.save(existingAirbnbIdentity);
    }

    private String generateSecret() {
        return securityService.getAlphaNumericString(42, false);
    }
}

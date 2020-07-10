package com.idetix.identityapprover.service.airbnb;

import com.idetix.identityapprover.entity.AirbnbIdentity;
import org.web3j.abi.datatypes.generated.Uint256;
import com.idetix.identityapprover.repository.AirbnbIdentityRepository;
import com.idetix.identityapprover.service.blockchain.BlockchainService;
import com.idetix.identityapprover.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.generated.Uint256;

@Service
public class AirbnbIdentityService {
    @Autowired
    private AirbnbIdentityRepository repository;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private BlockchainService blockchainService;

    // Method to creat a new Request to verify a new profileUrl Address
    // If the Address already exists, a new secret is Sent
    // If the Address is allready verified return false
    public String addAirbnbIdentity(String profileUrl) {
        if (getAirbnbIdentityById(profileUrl) == null) {
            AirbnbIdentity airbnbIdentity = new AirbnbIdentity(profileUrl, generateSecret(), "", false);
            saveAirbnbIdentityById(airbnbIdentity);
            return airbnbIdentity.getSecret();
            }
        AirbnbIdentity airbnbIdentity = getAirbnbIdentityById(profileUrl);
        if (airbnbIdentity.getVerified() == true) {
            return "allready verified";
        }
        airbnbIdentity.setSecret(generateSecret());
        updateAirbnbIdentity(airbnbIdentity);
        return airbnbIdentity.getSecret();
    }

    //Method to finaly verify the Airbnb Profile Address and make it unchangable in the Database
    // When the provided secret corrospond with the secret on the DB and the provided Signature is correct for the secret
    // and ETHAddress, the Identity gets verified on the Blockchain and is set to verified on the DB
    public AirbnbIdentity verifyAirbnbIdentity(String profileUrl, String ethAddress) {
        if (getAirbnbIdentityById(profileUrl) == null) {
            return null;
        }
        AirbnbIdentity airbnbIdentity = getAirbnbIdentityById(profileUrl);
        // TODO: 10.07.2020 get Signed String from Profile and check if verified
        String signedSecret = "yes";

        if (securityService.verifyAddressFromSignature(ethAddress, signedSecret, airbnbIdentity.getSecret())) {
            if (blockchainService.getSecurityLevelforAdress(ethAddress) < 3) {
                if (blockchainService.saveIdentityProofToChain(ethAddress, 3) == true) {
                    airbnbIdentity.setVerified(true);
                    airbnbIdentity.setEthAddress(ethAddress);
                    updateAirbnbIdentity(airbnbIdentity);
                }
            }
        }

        return airbnbIdentity;
    }

    private AirbnbIdentity getAirbnbIdentityById(String profileUrl) {
        return repository.findById(profileUrl).orElse(null);
    }

    private AirbnbIdentity saveAirbnbIdentityById(AirbnbIdentity airbnbIdentity) {
        return repository.save(airbnbIdentity);
    }

    private AirbnbIdentity updateAirbnbIdentity(AirbnbIdentity airbnbIdentity) {
        AirbnbIdentity existingAirbnbIdentity = repository.findById(airbnbIdentity.getProfileUrl()).orElse(null);
        existingAirbnbIdentity.setEthAddress(airbnbIdentity.getEthAddress());
        existingAirbnbIdentity.setSecret(airbnbIdentity.getSecret());
        existingAirbnbIdentity.setVerified(airbnbIdentity.getVerified());
        return repository.save(existingAirbnbIdentity);
    }

    private String generateSecret() {
        return securityService.getAlphaNumericString(42, false);
    }

}


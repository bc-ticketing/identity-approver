package com.idetix.identityapprover.controller;

import com.idetix.identityapprover.entity.Exceptions.BlockChainWriteFailedException;
import com.idetix.identityapprover.entity.Exceptions.SignatureMismatchException;
import com.idetix.identityapprover.entity.PhoneIdentity;
import com.idetix.identityapprover.service.phone.PhoneIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class PhoneIdentityController {

    @Autowired
    private PhoneIdentityService service;

    @PostMapping("/addPhoneIdentity")
    public boolean addPhoneIdentity(@RequestParam String phoneNr) {
        return service.addPhoneIdentity("+" + phoneNr);
    }

    @PostMapping("/PhoneIdentity")
    public PhoneIdentity approvePhoneIdentity(@RequestParam String phoneNr, @RequestParam String secret,
            @RequestParam String signedSecret, @RequestParam String ethAddress) {
        try {
            return service.verifyPhoneIdentity("+" + phoneNr, ethAddress, secret, signedSecret);
        }
        catch (SignatureMismatchException exc){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Provided Signature does not match", exc);
        } catch (BlockChainWriteFailedException exc) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Blockchain write Failed", exc);
        }
    }

}

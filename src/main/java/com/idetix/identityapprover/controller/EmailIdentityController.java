package com.idetix.identityapprover.controller;

import com.idetix.identityapprover.entity.EmailIdentity;
import com.idetix.identityapprover.entity.Exceptions.SignatureMismatchException;
import com.idetix.identityapprover.entity.Exceptions.BlockChainWriteFailedException;
import com.idetix.identityapprover.service.email.EmailIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class EmailIdentityController {

    @Autowired
    private EmailIdentityService service;

    @PostMapping("/addEmailIdentity")
    public boolean addEmailIdentity(@RequestParam String eMail) {
        return service.addEmailIdentity(eMail);
    }

    @PostMapping("/EmailIdentity")
    public EmailIdentity approveEmailIdentity(@RequestParam String eMail, @RequestParam String secret,
            @RequestParam String signedSecret, @RequestParam String ethAddress) {
        try {
            return service.verifyEmailIdentity(eMail, ethAddress, secret, signedSecret);
        }
        catch (SignatureMismatchException exc){
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "Provided Signature does not match", exc);
        } catch (BlockChainWriteFailedException exc) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_MODIFIED, "Blockchain write Failed", exc);
        }
    }

}

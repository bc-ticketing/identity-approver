package com.idetix.identityapprover.controller;


import com.idetix.identityapprover.entity.EmailIdentity;
import com.idetix.identityapprover.service.email.EmailIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class EmailIdentityController {



    @Autowired
    private EmailIdentityService service;

    @PostMapping("/addEmailIdentity")
    public boolean addEmailIdentity(@RequestParam String eMail ){
        return service.addEmailIdentity(eMail);
    }

    @PostMapping("/validateEmailIdentity")
    public EmailIdentity approveEmailIdentity(@RequestParam String eMail, @RequestParam String secret, @RequestParam String signedSecret, @RequestParam String ethAddress ){
        return service.verifyEmailIdentity(eMail,ethAddress,secret,signedSecret);
    }



}

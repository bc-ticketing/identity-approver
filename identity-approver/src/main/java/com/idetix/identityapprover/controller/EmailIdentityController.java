package com.idetix.identityapprover.controller;


import com.idetix.identityapprover.entity.EmailIdentity;
import com.idetix.identityapprover.service.EmailIdentityService;
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

    @PostMapping("/approveEmailIdentity")
    public EmailIdentity approveEmailIdentity(@RequestParam String eMail, @RequestParam String secret, @RequestParam String signedSecret, @RequestParam String ethAddress ){
        return service.approveEmailIdentity(eMail,ethAddress,secret,signedSecret);
    }

    @GetMapping("/EmailIdentitiy/{eMail}")
    public EmailIdentity findEmailIdentityById(@PathVariable String eMail){
        return service.getEmailIdentityById(eMail);
    }


}

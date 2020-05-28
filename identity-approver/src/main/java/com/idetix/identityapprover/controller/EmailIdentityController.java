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
    public EmailIdentity addEmilIdentity(@RequestBody EmailIdentity emailIdentity){
        return service.saveEmailIdentity(emailIdentity);
    }

    @GetMapping("/EmailIdentitiy/{eMail}")
    public EmailIdentity findEmailIdentityById(@PathVariable String eMail){
        return service.getEmailIdentityById(eMail);
    }


}

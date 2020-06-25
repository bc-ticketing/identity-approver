package com.idetix.identityapprover.controller;


import com.idetix.identityapprover.entity.EmailIdentity;
import com.idetix.identityapprover.entity.PhoneIdentity;
import com.idetix.identityapprover.service.email.EmailIdentityService;
import com.idetix.identityapprover.service.phone.PhoneIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PhoneIdentityController {



    @Autowired
    private PhoneIdentityService service;

    @PostMapping("/addPhoneIdentity")
    public boolean addPhoneIdentity(@RequestParam String phoneNr ){
        return service.addPhoneIdentity("+"+phoneNr);
    }

    @PostMapping("/validatePhoneIdentity")
    public PhoneIdentity approvePhoneIdentity(@RequestParam String phoneNr, @RequestParam String secret, @RequestParam String signedSecret, @RequestParam String ethAddress ){
        return service.verifyPhoneIdentity("+"+phoneNr,ethAddress,secret,signedSecret);
    }



}

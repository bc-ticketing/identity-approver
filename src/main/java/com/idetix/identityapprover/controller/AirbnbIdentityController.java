package com.idetix.identityapprover.controller;


import com.idetix.identityapprover.entity.AirbnbIdentity;
import com.idetix.identityapprover.service.airbnb.AirbnbIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AirbnbIdentityController {


    @Autowired
    private AirbnbIdentityService service;

    @PostMapping("/addAirbnbIdentity")
    public String addAirbnbIdentity(@RequestParam String profileUrl) {
        return service.addAirbnbIdentity(profileUrl);
    }

    @PostMapping("/validateAirbnbIdentity")
    public AirbnbIdentity approveAirbnbIdentity(@RequestParam String profileUrl, @RequestParam String ethAddress) {
        return service.verifyAirbnbIdentity(profileUrl, ethAddress );
    }


}

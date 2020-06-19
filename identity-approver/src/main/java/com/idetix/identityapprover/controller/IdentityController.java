package com.idetix.identityapprover.controller;

import com.idetix.identityapprover.entity.Identity;
import com.idetix.identityapprover.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IdentityController {

    @Autowired
    private IdentityService service;

    @PostMapping("/addIdentity")
    public Identity addIdentity(@RequestBody Identity identity){
        return service.saveIdentity(identity);
    }

    @PostMapping("/addIdentities")
    public List<Identity> addIdentity(@RequestBody List<Identity>  identities){
        return service.saveIdentities(identities);
    }

    @GetMapping("/identities")
    public List<Identity> findAllIdentities(){
        return service.getIdentities();
    }

    @GetMapping("/identitiy/{ethAddress}")
    public Identity findIdentityById(@PathVariable String ethAddress) {
        return service.getIdentityById(ethAddress);
    }

    @PutMapping("/update")
    public Identity updateIdentity(@RequestBody Identity identity){
        return service.updateIdentity(identity);
    }

    @DeleteMapping("/delete/{ethAdress}")
    public String deleteIdentity(@PathVariable String ethAddress){
        return service.deleteIdentity(ethAddress);
    }

}

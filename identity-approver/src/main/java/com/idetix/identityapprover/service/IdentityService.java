package com.idetix.identityapprover.service;


import com.idetix.identityapprover.entity.Identity;
import com.idetix.identityapprover.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdentityService {
    @Autowired
    private IdentityRepository repository;

    public Identity saveIdentity(Identity identity){
        return repository.save(identity);
    }

    public List<Identity> saveIdentities(List<Identity> identities){
        return repository.saveAll(identities);
    }

    public List<Identity> getIdentities(){
        return repository.findAll();
    }

    public Identity getIdentityById(String ethAddress){
        return repository.findById(ethAddress).orElse(null);
    }

    public String deleteIdentity(String ethAddress) {
        repository.deleteById(ethAddress);
        return "Identity removed !!"+ ethAddress;
    }

    public Identity updateIdentity(Identity identity){
        Identity existingIdentity = repository.findById(identity.getEthAddress()).orElse(null);
        existingIdentity.setEmail(identity.getEmail());
        existingIdentity.setHandyNr(identity.getHandyNr());
        existingIdentity.setAirBnB(identity.getAirBnB());
        return repository.save(existingIdentity);
    }
}

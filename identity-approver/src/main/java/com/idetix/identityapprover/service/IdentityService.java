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

    public Identity getIdentityById(int id){
        return repository.findById(id).orElse(null);
    }

    public String deleteIdentity(int id) {
        repository.deleteById(id);
        return "Identity removed !!"+ id;
    }

    public Identity updateIdentity(Identity identity){
        Identity existingIdentity = repository.findById(identity.getId()).orElse(null);
        existingIdentity.setEmail(identity.getEmail());
        existingIdentity.setHandyNr(identity.getHandyNr());
        existingIdentity.setAirBnB(identity.getAirBnB());
        return repository.save(existingIdentity);
    }
}

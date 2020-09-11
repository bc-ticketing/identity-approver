package com.idetix.identityapprover.repository;

import com.idetix.identityapprover.entity.KYCIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KYCIdentityRepository extends JpaRepository<KYCIdentity, String>{
}


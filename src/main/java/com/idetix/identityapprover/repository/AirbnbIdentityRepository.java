package com.idetix.identityapprover.repository;

import com.idetix.identityapprover.entity.AirbnbIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirbnbIdentityRepository extends JpaRepository<AirbnbIdentity, String> {
}

package com.idetix.identityapprover.repository;

import com.idetix.identityapprover.entity.PhoneIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneIdentityRepository extends JpaRepository<PhoneIdentity, String> {
}

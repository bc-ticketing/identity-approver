package com.idetix.identityapprover.repository;

import com.idetix.identityapprover.entity.EmailIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailIdentityRepository extends JpaRepository<EmailIdentity, String> {
}

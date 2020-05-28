package com.idetix.identityapprover.repository;

import com.idetix.identityapprover.entity.Identity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityRepository extends JpaRepository<Identity, Integer> {
}

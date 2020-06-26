package com.idetix.identityapprover.service;

import com.idetix.identityapprover.entity.EmailIdentity;
import com.idetix.identityapprover.service.email.EmailIdentityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EmailIdentityServiceTest {
    private static final String EMAIL = "nicolas.spielmann@uzh.ch";
    private static final String SECRET = "AAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String ETHADDRESS = "0x658e4Fe24b34589492b18B1A45294bE0601606A9";
    private static final boolean VERIFIED = false;
    private static final EmailIdentity EMAILIDENTITY = new EmailIdentity(EMAIL, SECRET, ETHADDRESS, VERIFIED);
    @Autowired
    private EmailIdentityService emailIdentityService;

    @Test
    void addEmailIdentity() {
        emailIdentityService.addEmailIdentity(EMAIL);
    }

    @Test
    void approveEmailIdentity() {
    }

    @Test
    void getEmailIdentityById() {
    }

    @Test
    void updateEmailIdentity() {
    }
}
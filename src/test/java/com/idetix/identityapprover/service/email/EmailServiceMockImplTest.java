package com.idetix.identityapprover.service.email;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceMockImplTest {
    private EmailService emailServicemock = new EmailServiceMockImpl();
    private static String EMAIL_VALID = "nicolas.spielmann@uzh.ch";
    private static String EMAIL_INVALID = "nicolas.spielmann.uzh.ch";
    private static String SECRET = "dsfsdfsdf";


    @Test
    void sendSecretViaEmailValid() {
        boolean valid =   emailServicemock.sendSecretViaEmail(EMAIL_VALID,SECRET);
        assertTrue(valid);
    }
    void sendSecretViaEmailInvalid() {
        boolean invalid = emailServicemock.sendSecretViaEmail(EMAIL_INVALID, SECRET);
        assertFalse(invalid);
    }
}
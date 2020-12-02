package com.idetix.identityapprover.service.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityServiceTest {
    private static final String INPUT = "010911";
    private static final String INPUT_WRONG = "010912";
    private static final int CHECKSUM = 0;


    @Test
    void getChecksum_VALID() {
        int calculatedChecksum = SecurityService.getChecksum(INPUT);
        boolean valid = calculatedChecksum == CHECKSUM;
        assertTrue(valid);
    }

    @Test
    void getChecksum_INVALID() {
        int calculatedChecksum = SecurityService.getChecksum(INPUT_WRONG);
        boolean valid = calculatedChecksum == CHECKSUM;
        assertFalse(valid);
    }
}
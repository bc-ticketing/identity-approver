package com.idetix.identityapprover.service;

import org.junit.jupiter.api.Test;

import static com.idetix.identityapprover.service.SecurityService.verifyAddressFromSignature;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecurityServiceTest {
    private static final String MESSAGE = "De Buchi isch de King" ;
    private static final String ADDRESS = "0x74fd35392f1c2a97c6080e0394b1995b0e63c8bf" ;
    private static final String ADDRESS_INVALID = "0x54fd35392f1c2a97c6080e0394b1995b0e63c8bf" ;
    private static final String SIGNATURE = "0x249b6cc439f58597b01d1424d3325922fa765bdefde84412018c9e92dfc5e8f566b138af77305d8e46df7e2d352635b910204fb10174eeae815b2255a842e26e1b" ;

    @Test
    public void testVerifyAddressFromSignature(){
        boolean verified = verifyAddressFromSignature(ADDRESS,SIGNATURE,MESSAGE);
        assertTrue(verified);
    }
    @Test
    public void testVerifyAddressFromSignature_InvalidVerification(){
        boolean verified = verifyAddressFromSignature(ADDRESS_INVALID,SIGNATURE,MESSAGE);
        assertFalse(verified);
    }

}

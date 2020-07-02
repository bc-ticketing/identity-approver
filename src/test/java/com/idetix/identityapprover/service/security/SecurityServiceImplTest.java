package com.idetix.identityapprover.service.security;


import org.junit.jupiter.api.Test;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecurityServiceImplTest {
    private static final String MESSAGE = "De Buchi isch de King";
    private static final String ADDRESS = "0x74fd35392f1c2a97c6080e0394b1995b0e63c8bf";
    private static final String ADDRESS_INVALID = "0x54fd35392f1c2a97c6080e0394b1995b0e63c8bf";
    private static final String SIGNATURE = "0x249b6cc439f58597b01d1424d3325922fa765bdefde84412018c9e92dfc5e8f566b138af77305d8e46df7e2d352635b910204fb10174eeae815b2255a842e26e1b";
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
    private static final int SECRET_LENGTH = 42;
    private SecurityService securityService = new SecurityServiceImpl();

    @Test
    public void testVerifyAddressFromSignature() {
        boolean verified = securityService.verifyAddressFromSignature(ADDRESS, SIGNATURE, MESSAGE);
        assertTrue(verified);
    }

    @Test
    public void testVerifyAddressFromSignature_InvalidVerification() {
        boolean verified = securityService.verifyAddressFromSignature(ADDRESS_INVALID, SIGNATURE, MESSAGE);
        assertFalse(verified);
    }

    @Test
    public void testgetAlphaNumericStringSame() {
        String RandomString = securityService.getAlphaNumericString(SECRET_LENGTH, false);
        String RandomString2 = securityService.getAlphaNumericString(SECRET_LENGTH, false);
        boolean verified = !RandomString.contentEquals(RandomString2);
        assertTrue(verified);
    }

    @Test
    public void testgetAlphaNumericStringRightLength() {
        String RandomString = securityService.getAlphaNumericString(SECRET_LENGTH, false);
        boolean verified = RandomString.length() == SECRET_LENGTH;
        assertTrue(verified);
    }

    @Test
    public void testgetAlphaNumericStringRightChars() {
        String RandomString = securityService.getAlphaNumericString(SECRET_LENGTH, false);
        CharacterIterator it = new StringCharacterIterator(RandomString);
        boolean verified = true;
        while (it.current() != CharacterIterator.DONE && verified == true) {
            verified = CHARS.contains("" + it.current());
            it.next();
        }
        assertTrue(verified);
    }


}

package com.idetix.identityapprover.service.security;

public interface SecurityService {
    public boolean verifyAddressFromSignature(String address, String signature, String message);
    public String getAlphaNumericString(int n);
}

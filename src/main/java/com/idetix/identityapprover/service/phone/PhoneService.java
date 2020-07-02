package com.idetix.identityapprover.service.phone;

public interface PhoneService {
    public boolean sendSecretViaSMS(String to, String secret);
}

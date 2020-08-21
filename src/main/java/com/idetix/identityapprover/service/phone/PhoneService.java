package com.idetix.identityapprover.service.phone;

public interface PhoneService {
    boolean sendSecretViaSMS(String to, String secret);
}

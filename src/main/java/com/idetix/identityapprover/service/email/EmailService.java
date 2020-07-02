package com.idetix.identityapprover.service.email;

public interface EmailService {
    public boolean sendSecretViaEmail(String to, String secret);
}

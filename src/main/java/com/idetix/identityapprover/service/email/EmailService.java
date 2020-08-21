package com.idetix.identityapprover.service.email;

public interface EmailService {
    boolean sendSecretViaEmail(String to, String secret);
}

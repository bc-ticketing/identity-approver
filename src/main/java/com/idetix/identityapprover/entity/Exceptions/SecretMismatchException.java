package com.idetix.identityapprover.entity.Exceptions;

public class SecretMismatchException extends Exception {
    public SecretMismatchException(String errorMessage){
        super(errorMessage);
    }
}

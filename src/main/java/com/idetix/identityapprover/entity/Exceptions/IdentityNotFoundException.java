package com.idetix.identityapprover.entity.Exceptions;

public class IdentityNotFoundException extends Exception {
    public IdentityNotFoundException(String errorMessage){
        super(errorMessage);
    }
}

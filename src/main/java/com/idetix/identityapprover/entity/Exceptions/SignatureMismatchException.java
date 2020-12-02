package com.idetix.identityapprover.entity.Exceptions;

public class SignatureMismatchException extends Exception {
    public SignatureMismatchException(String errorMessage){
        super(errorMessage);
    }
}

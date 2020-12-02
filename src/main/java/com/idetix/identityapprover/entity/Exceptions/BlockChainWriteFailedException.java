package com.idetix.identityapprover.entity.Exceptions;

public class BlockChainWriteFailedException extends Exception {
    public BlockChainWriteFailedException(String errorMessage){
        super(errorMessage);
    }
}

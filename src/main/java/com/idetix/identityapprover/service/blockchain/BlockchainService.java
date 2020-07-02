package com.idetix.identityapprover.service.blockchain;

public interface BlockchainService {
    public boolean SaveIdentityProofToChain(String ethAddress, int securityLevel);
}

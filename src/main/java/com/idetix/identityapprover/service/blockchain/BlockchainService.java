package com.idetix.identityapprover.service.blockchain;

public interface BlockchainService {
    boolean saveIdentityProofToChain(String ethAddress, int securityLevel);
    int getSecurityLevelForAddress(String ethAddress);
}

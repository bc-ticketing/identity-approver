package com.idetix.identityapprover.service.blockchain;

public interface BlockchainService {
    public boolean saveIdentityProofToChain(String ethAddress, int securityLevel);
    public int getSecurityLevelForAddress (String ethAddress);
}

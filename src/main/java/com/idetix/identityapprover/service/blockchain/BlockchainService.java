package com.idetix.identityapprover.service.blockchain;

import org.web3j.abi.datatypes.generated.Uint256;

public interface BlockchainService {
    public boolean saveIdentityProofToChain(String ethAddress, int securityLevel);
    public int getSecurityLevelforAdress (String ethAddress);
}

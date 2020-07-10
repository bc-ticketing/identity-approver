package com.idetix.identityapprover.service.blockchain;

import jnr.ffi.annotations.In;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Service
public class BlockchainServiceImpl implements BlockchainService {
    Web3j web3;
    Credentials credentials;
    String contractAddress;

    @SneakyThrows
    @Autowired
    public BlockchainServiceImpl(
            @Value("${BlockchainPath}") String blockchainPath,
            @Value("${BlockchainWalletPath}") String walletPath,
            @Value("${BlockchainWalletPSWD}") String walletPSWD,
            @Value("${IdentityContractAddress}") String contractAddress) {
        web3 = Web3j.build(new HttpService(blockchainPath));
        credentials = WalletUtils.loadCredentials(walletPSWD, walletPath);
        this.contractAddress = contractAddress;
    }

    @Override
    public boolean saveIdentityProofToChain(String ethAddress, int securityLevel) {
        try {
            Function function = new Function("approveIdentity", // Function name
                    Arrays.asList(new Address(ethAddress), new Uint(BigInteger.valueOf(securityLevel))), // Function input parameters
                    Collections.emptyList()); // Function returned parameters
            String txData = FunctionEncoder.encode(function);
            TransactionManager txManager = new RawTransactionManager(web3, credentials);

            String txHash = txManager.sendTransaction(
                    DefaultGasProvider.GAS_PRICE,
                    DefaultGasProvider.GAS_LIMIT,
                    contractAddress,
                    txData,
                    BigInteger.ZERO).getTransactionHash();

            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(
                    web3,
                    TransactionManager.DEFAULT_POLLING_FREQUENCY,
                    TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);

            TransactionReceipt txReceipt = receiptProcessor.waitForTransactionReceipt(txHash);
            System.out.println(txReceipt.getBlockNumber());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    @Override
    public int getSecurityLevelforAdress (String ethAddress) {
        try {
            Function function = new Function("getSecurityLevel", // Function name
                    Arrays.asList(new Address(credentials.getAddress()), new Address(ethAddress)), // Function input parameters
                    Collections.singletonList(new TypeReference<Uint256>() {})); // Function returned parameters

            String encodedFunction = FunctionEncoder.encode(function);
            EthCall response = web3.ethCall(
                    Transaction.createEthCallTransaction(credentials.getAddress(), contractAddress, encodedFunction),
            DefaultBlockParameterName.LATEST).sendAsync().get();

            List<Type> someTypes = FunctionReturnDecoder.decode(
                    response.getValue(), function.getOutputParameters());
            Uint256 uint256 = (Uint256) someTypes.get(0);
            return uint256.getValue().intValue();

        } catch (Exception e) {
            return -1;
        }
    }


}
package com.idetix.identityapprover.service.blockchain;

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
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
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
    final Web3j web3;
    final Credentials credentials;
    final String contractAddress;

    @SneakyThrows
    @Autowired
    public BlockchainServiceImpl(
            @Value("${BlockchainPath}") String blockchainPath,
            @Value("${BlockchainPrivatKey}") String privateKey,
            @Value("${IDENTITY_ADDRESS}") String contractAddress) {
        web3 = Web3j.build(new HttpService(blockchainPath));
        credentials = Credentials.create(privateKey);
        this.contractAddress = contractAddress;
    }

    @Override
    public boolean saveIdentityProofToChain(String ethAddress, int securityLevel) {
        try {
            Function function = new Function("approveIdentity", // Function name
                    Arrays.asList(new Address(ethAddress), new Uint8(BigInteger.valueOf(securityLevel))), // Function input parameters
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
    public int getSecurityLevelForAddress (String ethAddress) {
        try {
            Function function = new Function("getSecurityLevel", // Function name
                    Arrays.asList(new Address(credentials.getAddress()), new Address(ethAddress)), // input parameters
                    Collections.singletonList(new TypeReference<Uint256>() {})); // return parameters

            String encodedFunction = FunctionEncoder.encode(function);
            EthCall response = web3.ethCall(
                    Transaction.createEthCallTransaction(credentials.getAddress(), contractAddress, encodedFunction),
            DefaultBlockParameterName.LATEST).sendAsync().get();

            List<Type> someTypes = FunctionReturnDecoder.decode(
                    response.getValue(), function.getOutputParameters());
            Uint8 uint8= (Uint8) someTypes.get(0);
            return uint8.getValue().intValue();

        } catch (Exception e) {
            return -1;
        }
    }

}

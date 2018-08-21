package com.olam.node.service.infrastructure;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.*;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class OfflineEthereumServiceImpl extends GenericEthereumNode implements OfflineEthereumService {
    OfflineEthereumServiceImpl(String rpcUrl) {
        super(rpcUrl);

        assert (web3j != null);
    }

    @Override
    public Credentials loadCredentials(File keystoreFile, String password) throws IOException, CipherException {
        return WalletUtils.loadCredentials(password, keystoreFile);
    }

    @Override
    public RawTransaction buildDeployTx(
            String deployerAddress, String shipperAddress, String receiverAddress, long timeStamp
    ) throws ExecutionException, InterruptedException {
        BigInteger nonce = web3j.ethGetTransactionCount(deployerAddress, DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();

        String encodedConstructor = FunctionEncoder.encodeConstructor(
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(shipperAddress),
                new org.web3j.abi.datatypes.Address(receiverAddress),
                new org.web3j.abi.datatypes.generated.Uint256(timeStamp))
        );

        // using a regular transaction
        return RawTransaction.createContractTransaction(nonce, gasPrice, gasLimit, BigInteger.ZERO, "0x" + Transport.BINARY + encodedConstructor);
    }

    @Override
    public RawTransaction buildSubmitDocTx(String docName, String docUrl, List<String> recipients, List<byte[]> keyList) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public RawTransaction buildRequestDocTx(String docName) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public RawTransaction buildRequestDocTx(String docName, int docVersion) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public String signTransaction(RawTransaction rawTransaction, Credentials credentials) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Numeric.toHexString(signedMessage);
    }
}

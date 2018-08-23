package com.olam.node.service.infrastructure;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class OfflineEthereumServiceImpl extends GenericEthereumNode implements OfflineEthereumService {
    public OfflineEthereumServiceImpl(String rpcUrl) {
        super(rpcUrl);

        assert (web3j != null);
    }

    @Override
    public Credentials loadCredentials(File keystoreFile, String password) throws IOException, CipherException {
        return WalletUtils.loadCredentials(password, keystoreFile);
    }

    @Override
    public Credentials loadCredentials(String keystoreFilePath, String password) throws IOException, CipherException {
        return WalletUtils.loadCredentials(password, new File(keystoreFilePath));
    }

    @Override
    public RawTransaction buildDeployTx(
            String fromAddress, String shipperAddress, String receiverAddress, long timeStamp
    ) throws ExecutionException, InterruptedException {
        BigInteger nonce = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();

        String encodedConstructor = FunctionEncoder.encodeConstructor(
                Arrays.asList(new org.web3j.abi.datatypes.Address(shipperAddress),
                new org.web3j.abi.datatypes.Address(receiverAddress),
                new org.web3j.abi.datatypes.generated.Uint256(timeStamp))
        );

        // using a regular transaction
        return RawTransaction.createContractTransaction(nonce, gasPrice, gasLimit, BigInteger.ZERO, "0x" + Transport.BINARY + encodedConstructor);
    }

    @Override
    public RawTransaction buildSubmitDocTx(
            String fromAddress, String contractAddress, String docName, String docUrl, List<String> recipients, List<byte[]> keyList, long timeStamp
    ) throws ExecutionException, InterruptedException {
        BigInteger nonce = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();

        final Function function = new Function(
                Transport.FUNC_SUBMITDOCUMENT,
                Arrays.asList(new org.web3j.abi.datatypes.Utf8String(docName),
                        new org.web3j.abi.datatypes.Utf8String(docUrl),
                        new org.web3j.abi.datatypes.generated.Uint256(timeStamp),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                                org.web3j.abi.Utils.typeMap(recipients, org.web3j.abi.datatypes.Address.class)),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                                org.web3j.abi.Utils.typeMap(keyList, org.web3j.abi.datatypes.generated.Bytes32.class))),
                Collections.emptyList());

        String encodedFunction = FunctionEncoder.encode(function);

        return RawTransaction.createContractTransaction(nonce, gasPrice, gasLimit, BigInteger.ZERO, encodedFunction);
    }

    @Override
    public RawTransaction buildRequestDocTx(String fromAddress, String contractAddress, String docName) throws ExecutionException, InterruptedException {
        BigInteger nonce = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();

        final Function function = new Function(
                Transport.FUNC_REQUESTDOCUMENT,
                Arrays.asList(new org.web3j.abi.datatypes.Utf8String(docName)),
                Arrays.asList(
                        new TypeReference<Utf8String>() {},
                        new TypeReference<Uint256>() {},
                        new TypeReference<Address>() {},
                        new TypeReference<Uint256>() {},
                        new TypeReference<Bytes32>() {}
                )
        );

        String encodedFunction = FunctionEncoder.encode(function);

        return RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, encodedFunction);
    }

    @Override
    public RawTransaction buildRequestDocTx(String fromAddress, String contractAddress, String docName, int docVersion) throws ExecutionException, InterruptedException {
        BigInteger nonce = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();

        final Function function = new Function(
                Transport.FUNC_REQUESTDOCUMENT,
                Arrays.asList(
                        new org.web3j.abi.datatypes.Utf8String(docName),
                        new org.web3j.abi.datatypes.generated.Uint256(docVersion)),
                Arrays.asList(
                        new TypeReference<Utf8String>() {},
                        new TypeReference<Uint256>() {},
                        new TypeReference<Address>() {},
                        new TypeReference<Uint256>() {},
                        new TypeReference<Bytes32>() {}
                )
        );

        String encodedFunction = FunctionEncoder.encode(function);

        return RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, encodedFunction);
    }

    @Override
    public String signTransaction(RawTransaction rawTransaction, Credentials credentials) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Numeric.toHexString(signedMessage);
    }
}

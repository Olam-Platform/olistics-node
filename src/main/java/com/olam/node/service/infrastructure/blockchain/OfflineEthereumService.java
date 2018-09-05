package com.olam.node.service.infrastructure.blockchain;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.*;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class OfflineEthereumService {
    public static Credentials loadCredentials(File keystoreFile, String password) throws IOException, CipherException {
        return WalletUtils.loadCredentials(password, keystoreFile);
    }

    public static Credentials loadCredentials(String keystoreFilePath, String password) throws IOException, CipherException {
        return WalletUtils.loadCredentials(password, new File(keystoreFilePath));
    }

    public static RawTransaction buildDeployTx(
            String shipperAddress, String receiverAddress, long timeStamp, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit
            ) throws ExecutionException, InterruptedException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(shipperAddress),
                        new org.web3j.abi.datatypes.Address(receiverAddress),
                        new org.web3j.abi.datatypes.generated.Uint256(timeStamp))
        );

        // using a regular transaction
        return RawTransaction.createContractTransaction(nonce, gasPrice, gasLimit, BigInteger.ZERO, "0x" + Transport.BINARY + encodedConstructor);
    }

    public static RawTransaction buildSubmitDocTx(
            String fromAddress, String contractAddress, String docName, String docUrl, List<String> recipients, List<byte[]> keyList, long timeStamp, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit
    ) throws ExecutionException, InterruptedException {
        final Function function = new Function(
                Transport.FUNC_SUBMITDOCUMENT,
                Arrays.<Type>asList(
                        new org.web3j.abi.datatypes.Utf8String(docName),
                        new org.web3j.abi.datatypes.Utf8String(docUrl),
                        new org.web3j.abi.datatypes.generated.Uint256(timeStamp),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                                org.web3j.abi.Utils.typeMap(recipients, org.web3j.abi.datatypes.Address.class)
                        ),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                                org.web3j.abi.Utils.typeMap(keyList, org.web3j.abi.datatypes.generated.Bytes32.class)
                        )
                ),
                Collections.<TypeReference<?>>emptyList()
        );

        String encodedFunction = FunctionEncoder.encode(function);

        return RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, BigInteger.ZERO, encodedFunction);
    }

    public static String signTransaction(RawTransaction rawTransaction, Credentials credentials) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Numeric.toHexString(signedMessage);
    }
}
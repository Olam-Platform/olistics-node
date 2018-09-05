package com.olam.node.service.infrastructure.blockchain;

import org.web3j.crypto.Credentials;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface EthereumNodeService {

    String createAccount(String password) throws IOException;

    // deploy a Transport contract
    Transport deployTransportContract(Credentials credentials, String shipperAddress, String receiverAddress, long msecSinceEpoc) throws Exception;

    // load a Transport contract
    Transport loadTransportContract(Credentials credentials, String contractAddress);

    void submitDocument(Credentials credentials, String contractAddress, String docName, String docUrl);

    Tuple2<String, byte[]> requestDocument(String contractAddress, String docName);

    List<String> getAccounts() throws IOException;

    void sendEther(Credentials credentials, String recipient, float sum) throws Exception;

    float getEtherBalance(String accountAddress) throws IOException;

    // sending offline prepared and signed transactions
    String sendDeployTx(String signedTx);

    void sendSubmitDocTx(String signedTx);

    Tuple4<String, BigInteger, String, BigInteger> sendRequestDocCall(
            String fromAddress, String contractAddress, String docName
    ) throws IOException;

    Tuple4<String, BigInteger, String, BigInteger> sendRequestDocCall(
            String fromAddress, String contractAddress, String docName, int docVersion
    ) throws IOException;
    String getDocumentId(String shipmentId, String documentName);

//    boolean checkWritePermission(String signature, String shipmentId);

//    boolean checkWritePermission(Sign.SignatureData signature, String shipmentId);
}


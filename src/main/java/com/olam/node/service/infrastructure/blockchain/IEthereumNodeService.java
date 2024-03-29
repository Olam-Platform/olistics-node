package com.olam.node.service.infrastructure.blockchain;

import com.olam.node.service.application.entities.Collaborator;
import org.web3j.crypto.Credentials;
import org.web3j.tuples.generated.Tuple4;
import rx.Subscription;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IEthereumNodeService {
    Object syncObject = new Object();

    String createAccount(String password) throws IOException;

    BigInteger getNonce(String fromAddress) throws ExecutionException, InterruptedException;

    ShipmentContract deployShipmentContract(Credentials credentials, String shipmentName, Collaborator owner, Collaborator shipper, Collaborator consignee) throws Exception;

    ShipmentContract loadShipmentContract(Credentials credentials, String contractAddress);

    void submitDocument(Credentials credentials, String contractAddress, String docName, String docUrl);

    List<String> getAccounts() throws IOException;

    void sendEther(Credentials credentials, String recipient, float sum) throws Exception;

    float getEtherBalance(String accountAddress) throws IOException;

    String sendDeployTx(String signedTx);

    void sendSubmitDocTx(String signedTx);

    void sendMessage(String message, Credentials senderCredentials, String toAddress) throws ExecutionException, InterruptedException;

    void notifyTransport(Credentials senderCredentials, String toAddress, String shipmentContractAddress) throws ExecutionException, InterruptedException;

    Subscription registerForTransportCreatedEvent(ITransportObserver transportObserver);

    Subscription registerForTransportEvents(ITransportObserver transportObserver);

    Tuple4<String, BigInteger, String, BigInteger> sendRequestDocCall(
            String fromAddress, String contractAddress, String docName
    ) throws IOException;

    Tuple4<String, BigInteger, String, BigInteger> sendRequestDocCall(
            String fromAddress, String contractAddress, String docName, int docVersion
    ) throws IOException;

    void waitForEvent(ITransportObserver transportObserveralu) throws InterruptedException;

    //void registerForShipmentEvent(String shipmentId, String address) throws IOException;

    //void registerForDocumentEvent(Observer observer);

    //boolean checkWritePermission(String signature, String shipmentId);

    //boolean checkWritePermission(Sign.SignatureData signature, String shipmentId);
}


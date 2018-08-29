package com.olam.node.service.infrastructure.blockchain;

import com.olam.node.service.infrastructure.Transport;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.tuples.generated.Tuple2;

import java.io.IOException;
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
}

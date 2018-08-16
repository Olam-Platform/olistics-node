package com.olam.node.service.infrastructure;

import com.olam.node.sdk.Transport;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface EthereumNodeService {

    // deploy using online signature
    //Transport DeployTransportContract(String accountAddress, String password, String shipperAddress, String receiverAddress, long msecSinceEpoc) throws Exception;

    // deploy using offline signature
    Transport       DeployTransportContract(Credentials credentials, String shipperAddress, String receiverAddress, long msecSinceEpoc) throws Exception;

    Transport       LoadTransportContract(Credentials credentials, String contractAddress);

    String          CreateAccount(String password) throws IOException;

    List<String>    GetAccounts() throws IOException;

    Credentials     LoadCredentials(File keystoreFile, String password) throws IOException, CipherException;

    void            SendEther(Credentials credentials, String recipient, float sum) throws Exception;

    float           GetEtherBalance(String aacountAddress) throws IOException;

    String relaySignedTransaction(String signedTransaction);
}

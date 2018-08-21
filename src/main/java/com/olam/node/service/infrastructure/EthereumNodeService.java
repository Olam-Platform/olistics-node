package com.olam.node.service.infrastructure;

import org.web3j.crypto.Credentials;

import java.io.IOException;
import java.util.List;

public interface EthereumNodeService {
    String createAccount(String password) throws IOException;

    // deploy a Transport contract
    Transport deployTransportContract(Credentials credentials, String shipperAddress, String receiverAddress, long msecSinceEpoc) throws Exception;

    // load a Transport contract
    Transport loadTransportContract(Credentials credentials, String contractAddress);

    // get node's accounts
    List<String> getAccounts() throws IOException;

    void sendEther(Credentials credentials, String recipient, float sum) throws Exception;

    float getEtherBalance(String aacountAddress) throws IOException;

    // returns deployed contract address
    String sendDeployTransaction(String deployTransaction);

    //String sendTransaction(String signedTransaction);
}

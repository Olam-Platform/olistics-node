/*
package com.olam.node.sdk;

import com.olam.node.service.infrastructure.blockchain.OfflineEthereumService;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class Shipment implements IShipment {
    OfflineEthereumService offlineNodeService;

    public Shipment(String nodeRpcUrl) {
        offlineNodeService = new OfflineEthereumService();
    }

    @Override
    public String createShipment(Credentials credentials, String managerAddress, String shipperAddress, String receiverAddress) {
        long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();

        BigInteger nonce = BigInteger.ZERO;        // get it from the olma-node
        BigInteger gasPrice = BigInteger.valueOf(20000000000L);
        BigInteger gasLimit = BigInteger.valueOf(6721975);

        RawTransaction deployTx = offlineNodeService.buildDeployTx(shipperAddress, receiverAddress, msecSinceEpoc, nonce, gasPrice, gasLimit);
        String signedDeployTx = offlineNodeService.signTransaction(deployTx, credentials);

        /// ------------------------------------------------------------------------------------------------------------------
        /// now...
        /// 1. send the signed tx to the node
        /// 2. in the node use:
        ///        String contractAddress = EthereumNodeService.sendDeployTx(signedTransaction) to send the signed transaction
        ///    to deploy the contract and receive it's address
        ///    send the contract's address back to the user as a shipment id to be used as a reference for future actions
        /// ------------------------------------------------------------------------------------------------------------------

        return signedDeployTx;
    }

    @Override
    public Credentials loadCredentials(File keyStoreFile, String password) throws IOException, CipherException {
        return offlineNodeService.loadCredentials(keyStoreFile, password);
    }

    @Override
    public Credentials loadCredentials(String keyStoreFilePath, String password) throws IOException, CipherException {
        return offlineNodeService.loadCredentials(keyStoreFilePath, password);
    }
}
*/
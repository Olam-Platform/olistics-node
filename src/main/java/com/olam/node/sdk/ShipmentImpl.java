package com.olam.node.sdk;

import com.olam.node.service.infrastructure.OfflineEthereumServiceImpl;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class ShipmentImpl implements Shipment {
    OfflineEthereumServiceImpl offlineNodeService;

    public ShipmentImpl(String nodeRpcUrl) {
        offlineNodeService = new OfflineEthereumServiceImpl(nodeRpcUrl);
    }

    @Override
    public String createShipment(
            Credentials credentials, String managerAddress, String shipperAddress, String receiverAddress
    ) throws ExecutionException, InterruptedException {
        long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();

        RawTransaction deployTx = offlineNodeService.buildDeployTx(managerAddress, shipperAddress, receiverAddress, msecSinceEpoc);
        String signedDeployTx = offlineNodeService.signTransaction(deployTx, credentials);

        /// ------------------------------------------------------------------------------------------------------------------
        /// now...
        /// 1. send the signed tx to the node
        /// 2. in the node use:
        ///        String contractAddress = EthereumNodeServiceImpl.sendDeployTx(signedTransaction) to send the signed transaction
        ///    to deploy the contract and receive it's address
        ///    send the contract's address back to the user as a shipment id to be used as a reference for future actions
        /// ------------------------------------------------------------------------------------------------------------------

        return null;
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

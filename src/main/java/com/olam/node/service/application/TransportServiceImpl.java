package com.olam.node.service.application;

import com.olam.node.service.infrastructure.blockchain.EthereumNodeService;
import com.olam.node.service.infrastructure.storage.DataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Service
public class TransportServiceImpl implements TransportService {

    @Autowired
    private DataStorageService dataStorageService;
    @Autowired
    private EthereumNodeService ethereumNode;


    @Override
    public String createShipment(String deployShipmentTransaction) {

        return ethereumNode.sendDeployTx(deployShipmentTransaction);
    }

    @Override
    public String getDocumentId(byte[] document) {

        return dataStorageService.getdataIdentifier(document);
    }

    @Override
    public String uploadDocument(String submitDocumentTransaction, byte[] document) {

        String documentHash = dataStorageService.save(document);
        ethereumNode.sendSubmitDocTx(submitDocumentTransaction);
        return documentHash;

    }

    @Override
    public Resource downloadDocument(String shipmentId, String documentName) {

        String documentId = ethereumNode.getDocumentId(shipmentId, documentName);
        return dataStorageService.loadDataAsResource(documentId);
    }

    public BigInteger getNonce(String address) throws ExecutionException, InterruptedException {
        return ethereumNode.getNonce(address);
    }
}

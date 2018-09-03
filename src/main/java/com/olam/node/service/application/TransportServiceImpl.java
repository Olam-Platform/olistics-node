package com.olam.node.service.application;

import com.olam.node.service.infrastructure.blockchain.EthereumNodeService;
import com.olam.node.service.infrastructure.storage.DataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class TransportServiceImpl implements TransportService {

    @Autowired
    private DataStorageService dataStorageService;
    @Autowired
    private EthereumNodeService ethereumNode;


    @Override
    public String createShipment(String deployShipmentTransaction) {

        return null;
    }

    @Override
    public String getDocumentId(byte[] document) {
        return dataStorageService.getdataIdentifier(document);
    }

    @Override
    public String uploadDocument(String submitDocumentTransaction, byte[] document) {

        //todo: relay signed transaction to blockchain
        return dataStorageService.save(document);

    }

    @Override
    public Resource downloadDocument(String shipmentId, String documentName) {
        String documentId = null;
        //todo: get document hash from shipment contract
        return dataStorageService.loadDataAsResource(documentId);
    }
}

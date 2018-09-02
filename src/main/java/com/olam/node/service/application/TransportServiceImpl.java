package com.olam.node.service.application;

import com.olam.node.service.infrastructure.blockchain.EthereumNodeService;
import com.olam.node.service.infrastructure.storage.DataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public String GetDocumentId(byte[] document) {
        return dataStorageService.getdataIdentifier(document);
    }

    @Override
    public String uploadDocument(String submitDataTransaction, MultipartFile document) {
        return null;
    }

    @Override
    public Resource downloadDocument(String documentId) {
        return null;
    }
}

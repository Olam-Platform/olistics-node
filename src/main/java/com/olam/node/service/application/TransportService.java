package com.olam.node.service.application;

import com.olam.node.service.infrastructure.blockchain.EthereumNodeService;
import com.olam.node.service.infrastructure.storage.DataStorageService;
import org.springframework.beans.factory.annotation.Autowired;

public class TransportService {

    @Autowired
    private DataStorageService dataStorageService;
    @Autowired
    private EthereumNodeService ethereumNode;

}

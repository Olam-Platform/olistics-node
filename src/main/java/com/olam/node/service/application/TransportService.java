package com.olam.node.service.application;

import com.olam.node.service.application.entities.Participant;
import com.olam.node.service.infrastructure.DataStorageService;
import com.olam.node.service.infrastructure.EthereumNodeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TransportService {

    @Autowired
    private DataStorageService dataStorageService;
    @Autowired
    private EthereumNodeService ethereumNode;

    public void createShipment(List<Participant> participants, String signedTransaction) {
        //check valid participants
        //get public key from signed transaction
        //relay transaction to blockchain
        //ethereumNode.sendTransaction(signedTransaction);
    }
}

package com.olam.node.web;

import com.olam.node.service.application.*;
import com.olam.node.service.application.entities.Collaborator;
import com.olam.node.service.application.entities.Document;
import com.olam.node.service.application.entities.Shipment;
import com.olam.node.service.infrastructure.storage.IDataStorageService;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Credentials;

import java.net.URL;


@RestController
@RequestMapping("/v1/shipments")
public class ShipmentsController {
    private static final Logger logger = LoggerFactory.getLogger(ShipmentsController.class);

    @Autowired
    private IShipmentService shipmentService;
    @Autowired
    private EventsService eventsService;
    @Autowired
    private ICredentialsService credentialsService;
    @Autowired
    private IDataStorageService ipfsStorageService;

    private Detector detector = new DefaultDetector();

    @PostMapping()
    public String createShipment(@RequestParam  String shipmentName,
                                 @RequestParam  String ownerAddress, @RequestParam  String ownerName,
                                 @RequestParam  String shipperAddress, @RequestParam  String shipperName,
                                 @RequestParam  String consigneeAddress, @RequestParam  String consigneeName) {
        Credentials ownerCredentials = credentialsService.getCredentials(ownerAddress, "");

        Collaborator owner = new Collaborator(ownerName, IShipmentService.OWNER_ROLE, ownerAddress);
        Collaborator shipper = new Collaborator(shipperName, IShipmentService.SHIPPER_ROLE, shipperAddress);
        Collaborator consignee = new Collaborator(consigneeName, IShipmentService.CONSIGNEE_ROLE, consigneeAddress);

        String shipmentAddress = shipmentService.createShipment(ownerCredentials, shipmentName, owner, shipper, consignee);

        logger.info("created a shipment smart contract at address: " + shipmentAddress);

        return shipmentAddress;
    }

    @GetMapping
    public ResponseEntity<Shipment> getShipment(@RequestParam String fromAccount,
                                                @RequestParam String shipmentContractAddress) throws Exception {
        Credentials callerCredentials = credentialsService.getCredentials(fromAccount, "");

        Shipment shipmentEntity = shipmentService.getShipment(callerCredentials, shipmentContractAddress);

        return new ResponseEntity<>(shipmentEntity, HttpStatus.OK);
    }

    @PostMapping(value = "/documents")
    public String addDocument(@RequestParam String  fromAccount,
                              @RequestParam String  shipmentContractAddress,
                              @RequestParam String  docName,
                              @RequestParam String  docClientUrl,
                              @RequestParam String  collaboratorAddress) throws Exception {
        Credentials ownerCredentials = credentialsService.getCredentials(fromAccount, "");

        Collaborator owner = new Collaborator(fromAccount);
        Collaborator collaborator = new Collaborator(collaboratorAddress);
        Collaborator[] collaborators = new Collaborator[] {collaborator};

        Document shipmentDocument = new Document(docName, new URL(docClientUrl), owner, collaborators);

        boolean status = shipmentService.addDocument(ownerCredentials, shipmentDocument, shipmentContractAddress);

        return status? shipmentDocument.Url().toString() : "could not save document";
    }

    @PatchMapping (value = "/documents")
    public String updateDocument(@RequestParam String   fromAccount,
                                 @RequestParam String   shipmentContractAddress,
                                 @RequestBody Document  shipmentDocument) throws Exception {
        assert(fromAccount.equals(shipmentDocument.Owner()));

        Credentials ownerCredentials = credentialsService.getCredentials(fromAccount, "");

        boolean status = shipmentService.updateDocument(ownerCredentials, shipmentDocument, shipmentContractAddress);

        return status? shipmentDocument.Url().toString() : "could not save document";
    }

    @GetMapping(value = "/documents")
    public String getDocument(@RequestParam String  fromAccount,
                              @RequestParam String  shipmentContractAddress,
                              @RequestParam String  docName) throws Exception {
        Credentials ownerCredentials = credentialsService.getCredentials(fromAccount, "");

        return shipmentService.getDocument(ownerCredentials, shipmentContractAddress, docName);
    }
}

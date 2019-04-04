package com.olam.node.web;

//import com.olam.node.service.application.EventsService;
import com.olam.node.service.application.NotificationService;
import com.olam.node.service.application.entities.*;
import com.olam.node.service.infrastructure.storage.IDirectoryService;
import com.olam.node.service.application.IShipmentService;
import com.olam.node.service.infrastructure.storage.IPFSService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.tuples.generated.Tuple2;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

@Api(description = "manage shipments")
@RestController
@RequestMapping("/v1/shipments")
public class ShipmentsController {
    private static final Logger logger = LoggerFactory.getLogger(ShipmentsController.class);

    @Autowired
    private IShipmentService    shipmentService;
    @Autowired
    private IDirectoryService   directoryService;
    @Autowired
    private IPFSService         ipfsStorageService;


    private Detector detector = new DefaultDetector();

    @ApiOperation(value = "create a new shipment contract")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created shipment contract"),
            @ApiResponse(code = 400, message = "Illegal request parameter - probably unknown shipper or consignee"),
            @ApiResponse(code = 401, message = "Authentication error - userId not recognized or wrong password provided"),
            @ApiResponse(code = 404, message = "Owner, shipper or consignee unknown"),
            @ApiResponse(code = 500, message = "Blockchain down, db down or contract transaction failure")
    })
    @PostMapping
    public ResponseEntity<String> createShipment(@RequestParam  String shipmentName,
                                                 @RequestParam  String userId,
                                                 @RequestParam  String password,
                                                 @RequestParam  String shipperId,
                                                 @RequestParam  String consigneeId) {
        ResponseEntity<String> response;

        Credentials ownerCredentials;
        try {
            if (!directoryService.collaboratorExists(userId)) {
                response = new ResponseEntity<>("owner does not exists in directory", HttpStatus.NOT_FOUND);
            }
            else if(!directoryService.collaboratorExists(shipperId)) {
                response = new ResponseEntity<>("shipper does not exists in directory", HttpStatus.NOT_FOUND);
            }
            else if(!directoryService.collaboratorExists(consigneeId)) {
                response = new ResponseEntity<>("consignee does not exists in directory", HttpStatus.NOT_FOUND);
            }
            else {
                File ownerKeyStoreFile = new File(directoryService.getCollaboratorKeyStoreFilePath(userId));

                ownerCredentials = WalletUtils.loadCredentials(password, ownerKeyStoreFile);

                String ownerName = directoryService.getCollaboratorName(userId);
                String shipperName = directoryService.getCollaboratorName(shipperId);
                String consigneeName = directoryService.getCollaboratorName(consigneeId);

                Collaborator owner = new Collaborator(userId, ownerName, IShipmentService.OWNER_ROLE);
                Collaborator shipper = new Collaborator(shipperId, shipperName, IShipmentService.SHIPPER_ROLE);
                Collaborator consignee = new Collaborator(consigneeId, consigneeName, IShipmentService.CONSIGNEE_ROLE);

                Tuple2<String, BigInteger> shipmentAddressAndBlock = shipmentService.createShipment(ownerCredentials, shipmentName, owner, shipper, consignee);
                String shipmentId = shipmentAddressAndBlock.getValue1();

                logger.info("created a shipment smart contract at address: " + shipmentId);

                response = new ResponseEntity<>(shipmentId, HttpStatus.CREATED);

                // notify collaborators
                NotificationService.notify(directoryService.getCallbackUrl(shipperId), new Notification(shipmentId, userId, shipmentId));
                NotificationService.notify(directoryService.getCallbackUrl(consigneeId), new Notification(shipmentId, userId, consigneeId));
            }
        } catch (IOException e) {
            response = new ResponseEntity<>("server error", HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error(e.getMessage());
        } catch (CipherException e) {
            response = new ResponseEntity<>("error loading owner credentials", HttpStatus.UNAUTHORIZED);
            logger.error(e.getMessage());
        }

        return response;
    }


    @ApiOperation(value = "get shipment details by shipment id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Shipment information"),
            @ApiResponse(code = 400, message = "Illegal request parameter - probably unknown user"),
            @ApiResponse(code = 401, message = "Authentication error - userId not recognized or wrong password provided"),
            @ApiResponse(code = 404, message = "User unknown"),
            @ApiResponse(code = 500, message = "Blockchain down, db down or contract transaction failure")
    })
    @GetMapping
    public ResponseEntity<Shipment> getShipment(@RequestParam String userId,
                                                @RequestParam String password,
                                                @RequestParam String shipmentId) throws Exception {
        ResponseEntity<Shipment> response;

        Credentials collaboratorCredentials;
        try {
            if (!directoryService.collaboratorExists(userId)) {
                response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            else {
                File ownerKeyStoreFile = new File(directoryService.getCollaboratorKeyStoreFilePath(userId));

                collaboratorCredentials = WalletUtils.loadCredentials(password, ownerKeyStoreFile);

                // remove white spaces
                shipmentId = shipmentId.replaceAll("\\s","");

                Shipment shipmentEntity = shipmentService.getShipment(collaboratorCredentials, shipmentId);
                response = new ResponseEntity<>(shipmentEntity, HttpStatus.OK);
            }
        } catch (IOException e) {
            response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        } catch (CipherException e) {
            response = new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            e.printStackTrace();
        }

        return response;
    }
}

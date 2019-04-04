package com.olam.node.web;

import com.olam.node.service.application.IShipmentService;
import com.olam.node.service.application.NotificationService;
import com.olam.node.service.application.entities.Collaborator;
import com.olam.node.service.application.entities.Document;
import com.olam.node.service.application.entities.EventType;
import com.olam.node.service.application.entities.Notification;
import com.olam.node.service.infrastructure.storage.IDirectoryService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;

@Api(description = "manage shipment documents")
@RestController
@RequestMapping("/v1/documents")
public class DocumentsController {
    private static final Logger logger = LoggerFactory.getLogger(ShipmentsController.class);

    @Autowired
    private IShipmentService    shipmentService;
    //@Autowired
    //private EventsService       eventsService;
    @Autowired
    private IDirectoryService   directoryService;
    @Autowired
    private IPFSService         ipfsStorageService;
    private Detector            detector = new DefaultDetector();


    @ApiOperation(value = "add a document to a shipment. supply shipment id, doc name, collaborator id and document ftp url")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully added document to shipment"),
            @ApiResponse(code = 400, message = "Illegal request parameter - probably unknown user or collaborator"),
            @ApiResponse(code = 401, message = "Authentication error - userId not recognized or wrong password provided"),
            @ApiResponse(code = 404, message = "Owner or collaborator unknown"),
            @ApiResponse(code = 500, message = "Blockchain down, db down or contract transaction failure")
    })
    @PostMapping
    public ResponseEntity<String> submitDocument(@RequestParam String userId,
                                                 @RequestParam String password,
                                                 @RequestParam String shipmentId,
                                                 @RequestParam String docName,
                                                 @RequestParam String docUrl,
                                                 @RequestParam String recipientId) throws Exception {
        ResponseEntity<String> response;

        try {
            if (!directoryService.collaboratorExists(userId)) {
                response = new ResponseEntity<>("unknown user", HttpStatus.NOT_FOUND);
            }
            else if(!directoryService.collaboratorExists(recipientId)) {
                response = new ResponseEntity<>("unrecognized collaborator", HttpStatus.NOT_FOUND);
            }
            else {
                File ownerKeyStoreFile = new File(directoryService.getCollaboratorKeyStoreFilePath(userId));

                Credentials ownerCredentials;
                ownerCredentials = WalletUtils.loadCredentials(password, ownerKeyStoreFile);

                docUrl = docUrl.replace("\u202A", "");

                Document shipmentDocument = new Document(docName, docUrl, new Collaborator(userId), new Collaborator(recipientId));

                // remove white spaces
                shipmentId = shipmentId.replaceAll("\\s","");

                boolean status = shipmentService.addDocument(ownerCredentials, shipmentId, shipmentDocument);
                if(status) {
                    response = new ResponseEntity<>("document submitted", HttpStatus.OK);

                    // notify collaborator
                    // ...
                } else {
                    response = new ResponseEntity<>("error submitting document", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } catch (IOException e) {
            response = new ResponseEntity<>("error submitting document", HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error(e.getMessage());
        } catch (CipherException e) {
            response = new ResponseEntity<>("error loading user credentials", HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error(e.getMessage());
        }

        return response;
    }

    @ApiOperation(value = "request a shipment document. supply shipment id, document id and ftp url")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully downloaded a shipment document"),
            @ApiResponse(code = 400, message = "Illegal request parameter - probably unknown user or collaborator"),
            @ApiResponse(code = 401, message = "Authentication error - userId not recognized or wrong password provided"),
            @ApiResponse(code = 404, message = "User unknown"),
            @ApiResponse(code = 500, message = "Blockchain down, db down or contract transaction failure")
    })
    @GetMapping
    public ResponseEntity<Document> requestDocument(@RequestParam String  userId,
                                                    @RequestParam String  password,
                                                    @RequestParam String  shipmentId,
                                                    @RequestParam String  docName,
                                                    @RequestParam String  docUrl) throws Exception {
        ResponseEntity<Document> response;

        Credentials collaboratorCredentials;
        try {
            if (!directoryService.collaboratorExists(userId)) {
                response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            else {
                File ownerKeyStoreFilePath = new File(directoryService.getCollaboratorKeyStoreFilePath(userId));

                collaboratorCredentials = WalletUtils.loadCredentials(password, ownerKeyStoreFilePath);
                docUrl = docUrl.replace("\u202A", "");

                // remove white spaces
                shipmentId = shipmentId.replaceAll("\\s","");

                Document document = shipmentService.getDocument(collaboratorCredentials, shipmentId, docName, docUrl);

                response = new ResponseEntity<>(document, HttpStatus.OK);

                // notify collaborator
                // ...
            }

        } catch (IOException e) {
            response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error(e.getMessage());
        } catch (CipherException e) {
            response = new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            logger.error(e.getMessage());
        }

        return response;
    }
    /*
    @PatchMapping
    public ResponseEntity<String> updateDocument(@RequestParam String   userId,
                                                 @RequestParam String   password,
                                                 @RequestParam String   shipmentId,
                                                 @RequestParam String   docName,
                                                 @RequestParam String   docUrl) throws Exception {
        ResponseEntity<String> response;

        if (!directoryService.collaboratorExists(userId)) {
            response = new ResponseEntity<>("owner does not exists in directory", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else {
            File ownerKeyStoreFile = new File(directoryService.getCollaboratorKeyStoreFilePath(userId));

            Credentials ownerCredentials = null;
            try {
                ownerCredentials = WalletUtils.loadCredentials(password, ownerKeyStoreFile);
            } catch (IOException e) {
                logger.error(e.getMessage());
            } catch (CipherException e) {
                logger.error(e.getMessage());
            }

            docUrl = docUrl.replace("\u202A", "");

            boolean status = shipmentService.updateDocument(ownerCredentials, shipmentId, docName, docUrl);
            if(status) {
                response = new ResponseEntity<>("document updated", HttpStatus.OK);

                // notify collaborator
                Notification documentSubmittedNotification = new Notification(shipmentId, userId, EventType.DocumentEvent, docName + " updated");
                NotificationService.notify(directoryService.getCallbackUrl(userId), documentSubmittedNotification);
            } else {
                response = new ResponseEntity<>("error updating document", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return response;
    }
    */
}

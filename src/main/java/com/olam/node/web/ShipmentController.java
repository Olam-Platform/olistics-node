package com.olam.node.web;

import com.olam.node.service.application.TransportService;
import com.olam.node.service.application.entities.SubscribeData;
import com.olam.node.service.application.EventsService;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/v1/shipment")
public class ShipmentController {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentController.class);

    @Autowired
    private TransportService transportService;
    @Autowired
    private EventsService eventsService;

    private Detector detector = new DefaultDetector();


    @GetMapping(value = "/nonce/{address}")
    public String getNonce(@PathVariable String address) throws ExecutionException, InterruptedException {
        BigInteger nonce = transportService.getNonce(address);
        logger.debug(String.format("address %s returned with nonce %d", address, nonce.intValue()));
        return String.valueOf(nonce);
    }

    @PostMapping
    public String createShipment(@RequestBody String trx) {
        String address = transportService.createShipment(trx);
        logger.info("created a shipment smart contract at address: " + address);
        return address;
    }

    //endpoint for uploading Multipart documents (PDF, images, etc...)
    @PostMapping(value = "/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadNewMultipartDocument(@RequestPart("transaction") String transaction,
                                             @RequestParam("document") MultipartFile document) throws IOException {

        //check user permissions - next phase
        //send signed trx to blockchain + document to IPFS
        String docId = transportService.uploadDocument(transaction, document.getBytes());
        logger.debug(String.format("document %s, hash: %s was uploaded to shipment", document.getName(), docId));
        return docId;
    }

    @PostMapping(value = "/businessMessage", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public String uploadBusinessMessage(@RequestParam("uploadDocumentTransaction") String submitDocumentTransaction,
                                        @RequestParam("businessMessage") String businessMessage) {

        //check user permissions - next phase
        //send signed trx to blockchain + document to IPFS
        String result = transportService.uploadDocument(submitDocumentTransaction, businessMessage.getBytes());
        logger.info("uploaded document");
        return result;
    }

    //endpoint for Multipart documents (PDF, images, etc...)
    @PostMapping(value = "/document/id", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String GetMultipartDocumentId(@RequestParam MultipartFile document) throws IOException {
        //check user permissions - next phase
        //get document hash
        String id = transportService.getDocumentId(document.getBytes());
        logger.debug("document id calculated: " + id);
        return id;
    }

    //endpoint for XML, JSON documents (UBL messages)
    @PostMapping(value = "/businessMessage/id", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String GetBusinessMessageId(@RequestBody String businessMessage) {
        //check user permissions - next phase
        //get document hash
        String id = transportService.getDocumentId(businessMessage.getBytes());
        logger.debug("business message id calculated: " + id);
        return id;
    }


    @GetMapping(value = "/document")
    public byte[] downloadDocument(@RequestParam("address") String address, @RequestParam("shipmentId") String shipmentId,
                                   @RequestParam("docName") String docName) throws IOException {
        byte[] document = transportService.downloadDocument(address, shipmentId, docName);
        logger.debug(String.format("downloaded document: %s, from shipment: %s, document size: %d KB",
                docName, shipmentId, document.length / 1024));
        return document;
    }


    @GetMapping(value = "/businessMessage")
    public byte[] downloadBusinessMessage(@RequestParam("address") String address, @RequestParam("shipmentId") String shipmentId,
                                                            @RequestParam("messageName") String messageName) throws IOException {
        byte[] document = transportService.downloadDocument(address, shipmentId, messageName);
        logger.debug(String.format("downloaded messageName: %s, from shipment: %s, message size: %d KB",
                messageName, shipmentId, document.length / 1024));
        return document;
    }

    @PostMapping(value="/events")
    public ResponseEntity<String> subscribeToEvent(@RequestBody SubscribeData data){

        eventsService.saveCallbackUrl(data.getSignature(),data.getEvents().get(0),data.getCallbackUrl());

        logger.debug(String.format("user subscribed to event %s in shipment with url %s", data.getEvents().toString(), data.getCallbackUrl()));

        ResponseEntity entity = new ResponseEntity(String.format("subscription to [shipment: %s , event: %s, callback: %s]" +
                        " - succeeded!", data.getShipmentId(),data.getEvents(),data.getCallbackUrl() ),HttpStatus.CREATED);
        return entity;
    }

    private String detectDocumentType(InputStream stream) throws IOException {
        Metadata metadata = new Metadata();
        org.apache.tika.mime.MediaType mediaType = detector.detect(stream, metadata);
        return mediaType.toString();
    }
}

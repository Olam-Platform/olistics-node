package com.olam.node.web;

import com.olam.node.service.application.TransportService;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/v1/shipment")
public class ShipmentController {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentController.class);

    @Autowired
    private TransportService transportService;

    private Detector detector = new DefaultDetector();


    @PostMapping
    public String createShipment(@RequestBody String trx) {
        String address = transportService.createShipment(trx);
        logger.info("created a shipment smart contract at address: " + address);
        return address;
    }

    //endpoint for uploading Multipart documents (PDF, images, etc...)
    @PostMapping(value = "/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadNewMultipartDocument(@RequestParam("uploadDocumentTransaction") String submitDocumentTransaction,
                                             @RequestParam("document") MultipartFile document) throws IOException {

        //check user permissions - next phase
        //send signed trx to blockchain + document to IPFS
        return transportService.uploadDocument(submitDocumentTransaction, document.getBytes());
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
    @PostMapping(value = "/document/id", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String GetMultipartDocumentId(@RequestBody MultipartFile data) throws IOException {
        //check user permissions - next phase
        //get document hash
        String id = transportService.getDocumentId(data.getBytes());
        logger.debug("document id calculated: "+ id);
        return id;
    }

    //endpoint for XML, JSON documents (UBL messages)
    @PostMapping(value = "/businessMessage/id", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String GetBusinessMessageId(@RequestBody String businessMessage) {
        //check user permissions - next phase
        //get document hash
        String id = transportService.getDocumentId(businessMessage.getBytes());
        logger.debug("business message id calculated: "+ id);
        return id;    }

    @GetMapping(value = "/{shipmentId}/document/{documentName}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable("shipmentId") String shipmentId,
                                                     @PathVariable("documentName") String documentName) {

        Resource resource = transportService.downloadDocument(shipmentId, documentName);
        String contentType = null;
        try {
            contentType = this.detectDocumentType(resource.getInputStream());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping(value = "/{shipmentId}/businessMessage/{messageName}")
    public ResponseEntity<Resource> downloadBusinessMessage(@PathVariable("shipmentId") String shipmentId,
                                                            @PathVariable("messageName") String messageName) {
        return null;
    }

    private String detectDocumentType(InputStream stream) throws IOException {
        Metadata metadata = new Metadata();
        org.apache.tika.mime.MediaType mediaType = detector.detect(stream, metadata);
        return mediaType.toString();
    }
}

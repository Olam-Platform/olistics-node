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
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("shipment")
public class ShipmentController {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentController.class);

    @Autowired
    private TransportService transportService;

    private Detector detector = new DefaultDetector();


    @PostMapping
    public String createShipment(@RequestParam("deployTransaction") String trx) {
        //check permission to create shipment
        //check valid recipients
        //relay transaction to blockchain
        String address = transportService.createShipment(trx);
        //get transaction hash and send back to user
        return address;
    }


    @PostMapping("/document")
    public String submitNewDocument(@RequestParam("submitDocumentTransaction") String submitDocumentTransaction,
                                    @RequestParam("data") MultipartFile data) {

        //check user permissions - next phase
        //send signed trx to blockchain + document to IPFS
        return transportService.uploadDocument(submitDocumentTransaction, data);
    }

    @PostMapping(value = "/document/id", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String GetDataId(@RequestBody MultipartFile data) throws IOException {
        //check user permissions - next phase
        //get document hash
        return transportService.GetDocumentId(data.getBytes());
    }

    @PostMapping(value = "/document/id", consumes = {MediaType.APPLICATION_XML_VALUE,  MediaType.APPLICATION_JSON_VALUE})
    public String GetDataId(@RequestBody String data){
        //check user permissions - next phase
        //get document hash
        return transportService.GetDocumentId(data.getBytes());
    }

    @GetMapping("/document/{hash}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable("documentId") String documentId) {
        Resource resource = transportService.downloadDocument(documentId);
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

    private String detectDocumentType(InputStream stream) throws IOException {
        Metadata metadata = new Metadata();
        org.apache.tika.mime.MediaType mediaType = detector.detect(stream, metadata);
        return mediaType.toString();
    }
}

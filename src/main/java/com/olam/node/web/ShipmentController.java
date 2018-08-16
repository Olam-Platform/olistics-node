package com.olam.node.web;

import com.olam.node.service.infrastructure.DataStorageService;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("shipment")
public class ShipmentController {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentController.class);

    @Autowired
    private DataStorageService dataStorageService;
    private Detector detector = new DefaultDetector();


//    @PostMapping
//    public String createShipment(@RequestParam("recipients") String recipients, @RequestParam("signedTransaction") String trx){
//        //check permission to create shipment
//        //check valid recipients
//        //relay transaction to blockchain
//        //get transaction hash and send back to user
//
//    }

    @PostMapping
    public String submitNewData(@RequestParam("data") MultipartFile data) {
        String fileHash = null;
        try {
            fileHash = dataStorageService.save(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileHash;
    }

    @GetMapping(value = "/{hash}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("hash") String fileHash, HttpServletRequest request) {
        Resource resource = dataStorageService.loadDataAsResource(fileHash);
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

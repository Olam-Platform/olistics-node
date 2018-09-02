package com.olam.node.service.application;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface TransportService {

    String createShipment(String deployShipmentTransaction);

    String GetDocumentId(byte[] document);

    String uploadDocument(String submitDataTransaction, MultipartFile document);

    Resource downloadDocument(String documentId);

}

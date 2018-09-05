package com.olam.node.service.application;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface TransportService {

    String createShipment(String deployShipmentTransaction);

    String getDocumentId(byte[] document);

    String uploadDocument(String submitDocumentTransaction, byte[] document);

    Resource downloadDocument(String shipmentId, String documentName);

}

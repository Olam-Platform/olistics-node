package com.olam.node.service.application;

import org.springframework.core.io.Resource;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public interface TransportService {

    String createShipment(String deployShipmentTransaction);

    String getDocumentId(byte[] document);

    String uploadDocument(String submitDocumentTransaction, byte[] document);

    Resource downloadDocument(String shipmentId, String documentName);

    BigInteger getNonce(String address) throws ExecutionException, InterruptedException;
}

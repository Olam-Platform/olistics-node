package com.olam.node.service.application;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public interface TransportService {

    String createShipment(String deployShipmentTransaction);

    String getDocumentId(byte[] document);

    String uploadDocument(String submitDocumentTransaction, byte[] document);

    byte[] downloadDocument(String fromAddress, String shipmentId, String documentName) throws IOException;

    BigInteger getNonce(String address) throws ExecutionException, InterruptedException;

}

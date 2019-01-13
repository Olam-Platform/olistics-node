package com.olam.node.service.application;

import com.olam.node.service.application.entities.Collaborator;
import com.olam.node.service.application.entities.Document;
import com.olam.node.service.application.entities.Shipment;
import org.web3j.crypto.Credentials;

public interface IShipmentService {
    String OWNER_ROLE = "owner";
    String SHIPPER_ROLE = "shipper";
    String CONSIGNEE_ROLE = "consignee";

    String      createShipment(Credentials credentials, String shipmentName, Collaborator owner, Collaborator shipper, Collaborator consignee);

    Shipment    getShipment(Credentials credentials, String shipmentContractAddress) throws Exception;

    boolean     addDocument(Credentials credentials, Document document, String shipmentContractAddress) throws Exception ;

    boolean     updateDocument(Credentials credentials, Document document, String shipmentContractAddress) throws Exception ;

    String      getDocument(Credentials credentials, String shipmentContractAddress, String docName) throws Exception;

    /*
    String      getDocumentId(byte[] document);

    String      uploadDocument(String submitDocumentTransaction, byte[] document);

    byte[]      downloadDocument(String fromAddress, String shipmentId, String documentName) throws IOException;

    BigInteger  getNonce(String address) throws ExecutionException, InterruptedException;
    */
}

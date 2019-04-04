package com.olam.node.service.application;

import com.olam.node.service.application.entities.Collaborator;
import com.olam.node.service.application.entities.Document;
import com.olam.node.service.application.entities.Shipment;
import com.olam.node.service.infrastructure.storage.IDirectoryService;
import org.web3j.crypto.Credentials;
import org.web3j.tuples.generated.Tuple2;

import java.math.BigInteger;

public interface IShipmentService {
    String OWNER_ROLE = "owner";
    String SHIPPER_ROLE = "shipper";
    String CONSIGNEE_ROLE = "consignee";

    // return the shipment contract address and the block on which the deploy transaction was mined
    Tuple2<String, BigInteger> createShipment(Credentials credentials, String shipmentName, Collaborator owner, Collaborator shipper, Collaborator consignee);

    Shipment    getShipment(Credentials credentials, String shipmentContractAddress) throws Exception;

    boolean     addDocument(Credentials credentials, String shipmentContractAddress, Document document) throws Exception ;

    boolean     updateDocument(Credentials credentials, String shipmentContractAddress, String docName, String clientUrl) throws Exception ;

    Document    getDocument(Credentials credentials, String shipmentContractAddress, String docName, String docClientUrl) throws Exception;

    boolean     observe(String shipmentId, BigInteger blockNumber, IDirectoryService directoryService);
}

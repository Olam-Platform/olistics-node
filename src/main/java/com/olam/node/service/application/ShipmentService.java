package com.olam.node.service.application;

import com.olam.node.service.application.entities.Collaborator;
import com.olam.node.service.application.entities.Document;
import com.olam.node.service.application.entities.Shipment;
import com.olam.node.service.infrastructure.blockchain.IEthereumNodeService;
import com.olam.node.service.infrastructure.blockchain.ShipmentContract;
import com.olam.node.service.infrastructure.storage.IDirectoryService;
import com.olam.node.service.infrastructure.storage.IPFSService;
import com.olam.node.service.infrastructure.storage.IStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple3;
import rx.Subscription;

import java.math.BigInteger;


@Service
public class ShipmentService implements IShipmentService {
    private static final Logger logger = LoggerFactory.getLogger(ShipmentService.class);

    private IStorageService         dataStorageService;

    @Autowired
    private IEthereumNodeService    ethereumNode;
    @Autowired
    private IPFSService             ipfsService;

    public ShipmentService(IEthereumNodeService ethereumNode, IPFSService ipfsService) {
        this.ethereumNode = ethereumNode;
        this.ipfsService = ipfsService;
    }

    @Override
    public Tuple2<String, BigInteger> createShipment(Credentials credentials, String shipmentName, Collaborator owner, Collaborator shipper, Collaborator consignee) {
        Tuple2<String, BigInteger> shipmentProperties = null;

        try {
            Tuple2<ShipmentContract, BigInteger> shipmentTuple2 = ethereumNode.deployShipmentContract(credentials, shipmentName, owner, shipper, consignee);
            ShipmentContract shipmentContract = shipmentTuple2.getValue1();
            BigInteger inceptionBlockNumber = shipmentTuple2.getValue2();

            shipmentProperties = new Tuple2(shipmentContract.getContractAddress(), inceptionBlockNumber);

            shipmentContract.updateCollaborator(owner.Address(), owner.Name(), owner.Role()).sendAsync();
            shipmentContract.updateCollaborator(shipper.Address(), shipper.Name(), shipper.Role()).sendAsync();
            shipmentContract.updateCollaborator(consignee.Address(), consignee.Name(), consignee.Role()).sendAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shipmentProperties;
    }

    @Override
    public Shipment getShipment(Credentials credentials, String shipmentId) throws Exception {
        ShipmentContract shipmentContract = ethereumNode.loadShipmentContract(credentials, shipmentId);

        String contractState = shipmentContract.getState().send();
        String shipmentName = shipmentContract.getName().send();

        Tuple3<String, String, String> owner = shipmentContract.getCollaboratorByRole(IShipmentService.OWNER_ROLE).send();
        Tuple3<String, String, String> shipper = shipmentContract.getCollaboratorByRole(IShipmentService.SHIPPER_ROLE).send();
        Tuple3<String, String, String> consignee = shipmentContract.getCollaboratorByRole(IShipmentService.CONSIGNEE_ROLE).send();

        Collaborator[] collaborators = new Collaborator[] {
                new Collaborator(owner.getValue3(), owner.getValue1(), owner.getValue2()),
                new Collaborator(shipper.getValue3(), shipper.getValue1(), shipper.getValue2()),
                new Collaborator(consignee.getValue3(), consignee.getValue1(), consignee.getValue2())
        };

        return new Shipment(shipmentName, contractState, collaborators);
    }

    @Override
    public boolean addDocument(Credentials credentials, String shipmentId, Document document) throws Exception  {
        ShipmentContract shipmentContract = ethereumNode.loadShipmentContract(credentials, shipmentId);

        dataStorageService = IStorageService.getDataStorageService(document.Url());

        assert(!dataStorageService.equals(null));

        // read document from user's storage
        byte[] fileBytes = dataStorageService.read(document.Url());

        // store document in IPFS
        document.Url(ipfsService.save(fileBytes));

        // update document's url to be the IPFS hash
        TransactionReceipt txReceipt = shipmentContract.addDocument(document.Name(), document.Url(), document.Collaborator().Address()).send();

        return txReceipt.isStatusOK();
    }

    @Override
    public boolean updateDocument(Credentials credentials, String shipmentId, String docName, String clientUrl) throws Exception {
        ShipmentContract shipmentContract = ethereumNode.loadShipmentContract(credentials, shipmentId);
        TransactionReceipt txReceipt = shipmentContract.updateDocument(docName, clientUrl).send();

        return txReceipt.isStatusOK();
    }

    @Override
    public Document getDocument(Credentials credentials, String shipmentId, String docName, String docClientUrl) throws Exception {
        ShipmentContract shipmentContract = ethereumNode.loadShipmentContract(credentials, shipmentId);

        Tuple3<String, String, String> documentAttributes = shipmentContract.getDocument(docName).send();
        String ipfsDocHash = documentAttributes.getValue1();
        String ownerAddress = documentAttributes.getValue2();
        String collaboratorAddress = documentAttributes.getValue3();


        byte[] fileBytes = ipfsService.read(ipfsDocHash);

        dataStorageService = IStorageService.getDataStorageService(docClientUrl);
        dataStorageService.saveTo(fileBytes, docClientUrl);

        Collaborator owner = new Collaborator(ownerAddress);
        Collaborator collaborator = new Collaborator(collaboratorAddress);

        return new Document(docName, docClientUrl, owner, collaborator);
    }

    @Override
    public boolean observe(String shipmentId, BigInteger blockNumber, IDirectoryService directoryService) {
        Subscription subscription = ethereumNode.registerForShipmentEvents(shipmentId, blockNumber);

        return true;
    }
}

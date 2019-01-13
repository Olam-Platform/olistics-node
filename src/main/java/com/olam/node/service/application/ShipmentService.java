package com.olam.node.service.application;

import com.olam.node.service.application.entities.Collaborator;
import com.olam.node.service.application.entities.Document;
import com.olam.node.service.application.entities.Shipment;
import com.olam.node.service.infrastructure.blockchain.IEthereumNodeService;
import com.olam.node.service.infrastructure.blockchain.ShipmentContract;
import com.olam.node.service.infrastructure.storage.IDataStorageService;
import com.olam.node.service.infrastructure.storage.IPFSService;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;

@Service
public class ShipmentService implements IShipmentService {
    @Autowired
    private IDataStorageService     dataStorageService;
    @Autowired
    private IEthereumNodeService    ethereumNode;
    @Autowired
    private IPFSService ipfsService;

    public ShipmentService(IEthereumNodeService ethereumNode, IPFSService ipfsService) {
        this.ethereumNode = ethereumNode;
        this.ipfsService = ipfsService;
    }

    @Override
    public String createShipment(Credentials credentials, String shipmentName, Collaborator owner, Collaborator shipper, Collaborator consignee) {
        String shipmentContractAddress = "";

        try {
            ShipmentContract shipmentContract = ethereumNode.deployShipmentContract(credentials, shipmentName, owner, shipper, consignee);
            shipmentContractAddress = shipmentContract.getContractAddress();

            shipmentContract.updateCollaborator(owner.Address(), owner.Name(), owner.Role()).sendAsync();
            shipmentContract.updateCollaborator(shipper.Address(), shipper.Name(), shipper.Role()).sendAsync();
            shipmentContract.updateCollaborator(consignee.Address(), consignee.Name(), consignee.Role()).sendAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shipmentContractAddress;
    }

    @Override
    public Shipment getShipment(Credentials credentials, String shipmentContractAddress) throws Exception {
        ShipmentContract shipmentContract = ethereumNode.loadShipmentContract(credentials, shipmentContractAddress);

        String contractState = shipmentContract.getState().send();
        String shipmentName = shipmentContract.getName().send();

        Tuple3<String, String, String> owner = shipmentContract.getCollaboratorByRole(IShipmentService.OWNER_ROLE).send();
        Tuple3<String, String, String> shipper = shipmentContract.getCollaboratorByRole(IShipmentService.SHIPPER_ROLE).send();
        Tuple3<String, String, String> consignee = shipmentContract.getCollaboratorByRole(IShipmentService.CONSIGNEE_ROLE).send();

        Collaborator[] collaborators = new Collaborator[] {
                new Collaborator(owner.getValue1(), owner.getValue2(), owner.getValue3()),
                new Collaborator(shipper.getValue1(), shipper.getValue2(), shipper.getValue3()),
                new Collaborator(consignee.getValue1(), consignee.getValue2(), consignee.getValue3())
        };

        return new Shipment(shipmentName, contractState, collaborators);
    }

    @Override
    public boolean addDocument(Credentials credentials, Document document, String shipmentContractAddress) throws Exception  {
        ShipmentContract shipmentContract = ethereumNode.loadShipmentContract(credentials, shipmentContractAddress);

        /*
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect();
        String url = ipfsService.save("file saved to ipfs".getBytes());
        document.Url(url);
        */
        TransactionReceipt txReceipt = shipmentContract.addDocument(document.Name(), document.Url().toString(), document.Collaborators()[0].Address()).send();

        return txReceipt.isStatusOK();
    }

    @Override
    public boolean updateDocument(Credentials credentials, Document document, String shipmentContractAddress) throws Exception {
        ShipmentContract shipmentContract = ethereumNode.loadShipmentContract(credentials, shipmentContractAddress);
        TransactionReceipt txReceipt = shipmentContract.updateDocument(document.Name(), document.Url().toString()).send();

        return txReceipt.isStatusOK();
    }

    @Override
    public String getDocument(Credentials credentials, String shipmentContractAddress, String docName) throws Exception {
        ShipmentContract shipmentContract = ethereumNode.loadShipmentContract(credentials, shipmentContractAddress);
        String documentUrl = shipmentContract.getDocumentUrl(docName).send();

        return documentUrl;
    }

    //region unused
    /*
    @Override
    public String getDocumentId(byte[] document) {
        return dataStorageService.getDataIdentifier(document);
    }

    @Override
    public String uploadDocument(String submitDocumentTransaction, byte[] document) {
        String documentHash = dataStorageService.save(document);
        ethereumNode.sendSubmitDocTx(submitDocumentTransaction);

        return documentHash;
    }

    @Override
    public byte[] downloadDocument(String fromAddress, String shipmentId, String documentName) throws IOException {
        byte[] data = null;
        Tuple4<String, BigInteger, String, BigInteger> documentMetaData = ethereumNode.sendRequestDocCall(fromAddress, shipmentId, documentName);
        if (documentMetaData != null) {
            data = dataStorageService.loadData(documentMetaData.getValue1());
        }

        return data;
    }

    public BigInteger getNonce(String address) throws ExecutionException, InterruptedException {
        return ethereumNode.getNonce(address);
    }
    */
    //endregion
}

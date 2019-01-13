package com.olam.node.service.application;

import com.olam.node.service.application.entities.Collaborator;
import com.olam.node.service.application.entities.Document;
import com.olam.node.service.application.entities.Shipment;
import com.olam.node.service.infrastructure.blockchain.EthereumNodeService;
import com.olam.node.service.infrastructure.blockchain.IEthereumNodeService;
import com.olam.node.service.infrastructure.storage.IPFSService;
import com.olam.node.web.ShipmentsController;
import io.ipfs.api.IPFS;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.tx.exceptions.ContractCallException;

import java.math.BigInteger;
import java.net.ConnectException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ShipmentServiceTest {
    // test params
    private String ownerAddress     = "0x56F08C1046476D29236C3f4B0786923Bbb26a7a2";
    private String shipperAddress   = "0x702337329C0A73986D99510A0E937AfB0c9fF5Ad";
    private String consigneeAddress = "0x03CBD79019Bdbaf54d225859d8385983f5b25639";
    private String anonymAddress    = "0x2de3c14A2207a6C6295CCFc422ce56De5D5a17CC";
    private static String ganacheNodeUrl   = "http://127.0.0.1:7545";
    private static String ipfsNodeAddr     = "/dnsaddr/ipfs.infura.io/tcp/5001/https";

    private static BigInteger gasPrice     = BigInteger.valueOf(20000000000l);
    private static BigInteger gasLimit     = BigInteger.valueOf(6721975l);

    private String SHIPMENT_NAME    = " Jamaican Weed";

    private static IShipmentService shipmentService;
    private static ICredentialsService credentialsService;
    private static IEthereumNodeService ethereumNodeService;

    private static final Logger logger = LoggerFactory.getLogger(ShipmentsController.class);

    @BeforeClass
    public static void setUp() {
        credentialsService = new CredentialsService();

        try {
            ethereumNodeService = new EthereumNodeService(ganacheNodeUrl, gasPrice, gasLimit);
        } catch (ConnectException e) {
            e.printStackTrace();
        }

        IPFSService ipfsService = new IPFSService(new IPFS(ipfsNodeAddr));
        shipmentService = new ShipmentService(ethereumNodeService, ipfsService);
    }

    @Test
    public void testCreateShipment() {
        Collaborator owner = new Collaborator("Owner", "owner", ownerAddress);
        Collaborator shipper = new Collaborator("Shipper", "shipper", shipperAddress);
        Collaborator consignee = new Collaborator("Consignee", "consignee", consigneeAddress);

        String shipmentContractAddress = createShipment(SHIPMENT_NAME, owner, shipper, consignee);

        try {
            Credentials ownerCredentials = credentialsService.getCredentials(ownerAddress, "");
            Shipment shipment = shipmentService.getShipment(ownerCredentials, shipmentContractAddress);
            assertEquals(shipment.Name(), SHIPMENT_NAME);
            assertEquals(shipment.State(), "started");

            Credentials shipperCredentials = credentialsService.getCredentials(shipperAddress, "");
            shipment = shipmentService.getShipment(shipperCredentials, shipmentContractAddress);
            assertEquals(shipment.Name(), SHIPMENT_NAME);
            assertEquals(shipment.State(), "started");

            Credentials consigneeCredentials = credentialsService.getCredentials(consigneeAddress, "");
            shipment = shipmentService.getShipment(consigneeCredentials, shipmentContractAddress);
            assertEquals(shipment.Name(), SHIPMENT_NAME);
            assertEquals(shipment.State(), "started");

            Credentials anonymCredentials = credentialsService.getCredentials(anonymAddress, "");
            shipment = shipmentService.getShipment(anonymCredentials, shipmentContractAddress);
            assertEquals(shipment.Name(), SHIPMENT_NAME);
            assertEquals(shipment.State(), "started");

        } catch (Exception e) {
            assertEquals(e.getClass(), ContractCallException.class);
            assertEquals(e.getMessage(), "Empty value (0x) returned from contract");
        }
    }

    @Test
    public void testAddGetDocument() {
        final String DOC_NAME = "AWB";
        Collaborator owner = new Collaborator("Owner", "owner", ownerAddress);
        Collaborator shipper = new Collaborator("Shipper", "shipper", shipperAddress);
        Collaborator consignee = new Collaborator("Consignee", "consignee", consigneeAddress);

        String shipmentContractAddress = createShipment(SHIPMENT_NAME, owner, shipper, consignee);

        Collaborator[] collaborators = new Collaborator[]{shipper};

        try {
            Document document = new Document(DOC_NAME,  new URL("c:\\Users\\Eli\\Documents\\"), owner, collaborators);

            boolean docAdded = shipmentService.addDocument(credentialsService.getCredentials(owner.Address(), ""), document, shipmentContractAddress);
            assertTrue(docAdded);

            String ipfsHAsh = shipmentService.getDocument(credentialsService.getCredentials(shipper.Address(), ""), shipmentContractAddress, DOC_NAME);
            logger.info("ipfs hash for " + DOC_NAME + " is " + ipfsHAsh);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateDocument() {
    }

    private String createShipment(String name, Collaborator owner, Collaborator shipper, Collaborator consignee) {
        return shipmentService.createShipment(credentialsService.getCredentials(owner.Address(), ""), name, owner, shipper, consignee);
    }
}
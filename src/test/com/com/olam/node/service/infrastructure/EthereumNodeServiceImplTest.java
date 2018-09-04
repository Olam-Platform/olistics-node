package com.olam.node.service.infrastructure;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.RawTransaction;
import org.web3j.tuples.generated.Tuple4;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


public class EthereumNodeServiceImplTest extends OfflineEthereumServiceImplTest {
    static private EthereumNodeServiceImpl nodeService = null;

    @BeforeClass
    public static void setup() {
        try {
            OfflineEthereumServiceImplTest.setup();

            nodeService = new EthereumNodeServiceImpl(properties.getProperty(RPC_URL));

            /*
            if (nodeService.getEtherBalance(shipperCredentials.getAddress()) < 0.25) {
                nodeService.sendEther(managerCredentials, shipperCredentials.getAddress(), 0.5f);
            }
            if (nodeService.getEtherBalance(receiverCredentials.getAddress()) < 0.25) {
                nodeService.sendEther(managerCredentials, receiverCredentials.getAddress(), 0.5f);
            }
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void deployContract() {
        try {
            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
            lastDeployedContract = nodeService.deployTransportContract(managerCredentials, shipperCredentials.getAddress(), receiverCredentials.getAddress(), msecSinceEpoc);
            assertNotNull(lastDeployedContract);

            verifyContractInitialState(lastDeployedContract);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadContract() {
        try {
            Transport contract = nodeService.loadTransportContract(managerCredentials, lastDeployedContract.getContractAddress());
            assertNotNull(contract);

            verifyContractInitialState(contract);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSubmitRequestDoc() {
        try {
            Transport transportContract = nodeService.loadTransportContract(managerCredentials, lastDeployedContract.getContractAddress());

            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
            List<String> recipients = Arrays.asList(managerCredentials.getAddress(), shipperCredentials.getAddress(), receiverCredentials.getAddress());

            byte[] symmetricKey1 = new byte[32];
            byte[] symmetricKey2 = new byte[32];
            byte[] symmetricKey3 = new byte[32];
            List<byte[]> keys = Arrays.asList(symmetricKey1, symmetricKey2, symmetricKey3);

            transportContract.submitDocument("AirwayBill", "AirwayBill hash from IPFS", BigInteger.valueOf(msecSinceEpoc), recipients, keys).send();
            Tuple4<String, BigInteger, String, BigInteger/*, byte[]*/> document = transportContract.requestDocument("AirwayBill").send();

            assert (document.getValue1().equals("AirwayBill hash from IPFS"));      // verify url
            assert (document.getValue2().intValue() == 0);                          // verify version
            assert (document.getValue3().equals(managerCredentials.getAddress()));  // verify submitter address
            assert (document.getValue4().longValue() == msecSinceEpoc);             // verify timestamp

            transportContract.submitDocument("AirwayBill", "AirwayBill hash from IPFS", BigInteger.valueOf(msecSinceEpoc), recipients, keys).send();
            document = transportContract.requestDocument("AirwayBill").send();

            assert (document.getValue1().equals("AirwayBill hash from IPFS"));      // verify url
            assert (document.getValue2().intValue() == 1);                          // verify version
            assert (document.getValue3().equals(managerCredentials.getAddress()));  // verify submitter address
            assert (document.getValue4().longValue() == msecSinceEpoc);             // verify timestamp
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOfflineSubmitDoc() {
        final String DOC_NAME = "Payment request";
        final String DOC_URL = "some IPFS shit-hash";
        String contractAddress = lastDeployedContract.getContractAddress();

        try {
            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
            List<String> recipientsAddresses = Arrays.asList(shipperCredentials.getAddress(), receiverCredentials.getAddress());
            List<byte[]> keys = new ArrayList<>();

            byte[] key1 = new byte[32];
            byte[] key2 = new byte[32];

            keys.add(key1);
            keys.add(key2);

            for (int i = 0 ; i < 2 ; i++) {
                RawTransaction submitDcoTx = nodeService.buildSubmitDocTx(
                        managerCredentials.getAddress(), contractAddress, DOC_NAME, DOC_URL, recipientsAddresses, keys, msecSinceEpoc
                );

                assertNotNull(submitDcoTx);

                String signedTransaction = nodeService.signTransaction(submitDcoTx, managerCredentials);
                assertNotNull(signedTransaction);
                assertFalse(signedTransaction.isEmpty());

                nodeService.sendSubmitDocTx(signedTransaction);

                Tuple4<String, BigInteger, String, BigInteger> document1 = nodeService.loadTransportContract(
                        shipperCredentials, contractAddress).requestDocument(DOC_NAME).send();

                assert (document1.getValue1().equals(DOC_URL));                         // verify url
                assert (document1.getValue2().intValue() == i);                          // verify version
                assert (document1.getValue3().equals(managerCredentials.getAddress()));  // verify submitter address
                assert (document1.getValue4().longValue() == msecSinceEpoc);             // verify timestamp
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOfflineRequestDoc() {
        final String DOC_NAME = "Payment request";
        final String DOC_URL = "some IPFS shit-hash";
        String contractAddress = lastDeployedContract.getContractAddress();

        try {
            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
            List<String> recipientsAddresses = Arrays.asList(shipperCredentials.getAddress(), receiverCredentials.getAddress());
            List<byte[]> keys = new ArrayList<>();

            byte[] key1 = new byte[32];
            byte[] key2 = new byte[32];

            keys.add(key1);
            keys.add(key2);

            for (int i = 0 ; i < 2 ; i++) {
                nodeService.loadTransportContract(
                        managerCredentials, contractAddress
                ).submitDocument(DOC_NAME, DOC_URL, BigInteger.valueOf(msecSinceEpoc), recipientsAddresses, keys).send();

                Tuple4<String, BigInteger, String, BigInteger> document1 = nodeService.sendRequestDocCall(
                        shipperCredentials.getAddress(), contractAddress, DOC_NAME
                );

                assert (document1.getValue1().equals(DOC_URL));                         // verify url
                assert (document1.getValue2().intValue() == i);                          // verify version
                assert (document1.getValue3().equals(managerCredentials.getAddress()));  // verify submitter address
                assert (document1.getValue4().longValue() == msecSinceEpoc);             // verify timestamp
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOfflineDeployContract() {
        try {
            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
            RawTransaction deployTx = nodeService.buildDeployTx(
                    managerCredentials.getAddress(), shipperCredentials.getAddress(), receiverCredentials.getAddress(), msecSinceEpoc
            );
            assertNotNull(deployTx);

            String signedTx = nodeService.signTransaction(deployTx, managerCredentials);
            assertNotNull(signedTx);
            assertFalse(signedTx.isEmpty());

            String contractAddress = nodeService.sendDeployTx(signedTx);
            assertNotNull(contractAddress);
            assertFalse(contractAddress.isEmpty());

            Transport contract = nodeService.loadTransportContract(managerCredentials, contractAddress);

            verifyContractInitialState(contract);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void verifyContractInitialState(Transport contract) {
        try {
            // verify state
            assert (contract.getState().send().equals("none"));

            // verify address => role
            assert (contract.getRole(managerCredentials.getAddress()).send().equals("manager"));
            assert (contract.getRole(shipperCredentials.getAddress()).send().equals("shipper"));
            assert (contract.getRole(receiverCredentials.getAddress()).send().equals("receiver"));

            // verify role => address
            assert (contract.getAddress("manager").send().equals(managerCredentials.getAddress()));
            assert (contract.getAddress("shipper").send().equals(shipperCredentials.getAddress()));
            assert (contract.getAddress("receiver").send().equals(receiverCredentials.getAddress()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
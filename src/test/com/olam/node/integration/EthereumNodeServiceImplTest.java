package com.olam.node.integration;

import com.olam.node.service.infrastructure.blockchain.EthereumNodeServiceImpl;
import com.olam.node.service.infrastructure.blockchain.Transport;
import com.olam.node.service.infrastructure.blockchain.TransportObserverImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.RawTransaction;
import org.web3j.tuples.generated.Tuple4;
import rx.Subscription;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


public class EthereumNodeServiceImplTest extends OfflineEthereumServiceTest {
    static private EthereumNodeServiceImpl nodeService = null;
    private final Logger logger = LoggerFactory.getLogger(EthereumNodeServiceImplTest.class);

    @BeforeClass
    public static void setup() {
        try {
            OfflineEthereumServiceTest.setup();

            //nodeService = new EthereumNodeServiceImpl(RPC_URL, GAS_PRICE, GAS_LIMIT, NODE_USR, NODE_PASS);
            nodeService = new EthereumNodeServiceImpl(RPC_URL, GAS_PRICE, GAS_LIMIT);

            if (nodeService.getEtherBalance(shipperCredentials.getAddress()) < 0.2) {
                nodeService.sendEther(managerCredentials, shipperCredentials.getAddress(), 1);
            }

            if (nodeService.getEtherBalance(receiverCredentials.getAddress()) < 0.2) {
                nodeService.sendEther(managerCredentials, receiverCredentials.getAddress(), 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void deployContract() {
        try {
            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
            lastDeployedContract = nodeService.deployTransportContract(
                    managerCredentials, shipperCredentials.getAddress(), receiverCredentials.getAddress(), msecSinceEpoc
            );

            assertNotNull(lastDeployedContract);

            verifyContractInitialState(lastDeployedContract);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadContract() {
        try {
            assertNotNull(lastDeployedContract);

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
            assertNotNull(lastDeployedContract);

            Transport transportContract = nodeService.loadTransportContract(managerCredentials, lastDeployedContract.getContractAddress());

            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
            List<String> recipients = Arrays.asList(
                    managerCredentials.getAddress(), shipperCredentials.getAddress(), receiverCredentials.getAddress()
            );

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
                BigInteger nonce = nodeService.getNonce(managerCredentials.getAddress());
                RawTransaction submitDcoTx = nodeService.buildSubmitDocTx(contractAddress, DOC_NAME, DOC_URL, recipientsAddresses, keys, msecSinceEpoc,
                        nonce, GAS_PRICE, GAS_LIMIT
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
        final String DOC_URL  = "some IPFS shit-hash";

        assertNotNull(lastDeployedContract);
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

                assert (document1.getValue1().equals(DOC_URL));                          // verify url
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
    public void testTransportCreatedEvent() {
        try {
            TransportObserverImpl transportObserver = new TransportObserverImpl(shipperCredentials.getAddress());
            Subscription subscription = nodeService.registerForTransportCreatedEvent(transportObserver);

            Thread waiter = new Thread(() -> {
                synchronized (transportObserver) {
                    try {
                        logger.info(">>>>>> in thread: wait for transport created event");
                        transportObserver.wait();

                        logger.info(">>>>>> in thread: caught transport created event");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            logger.info(">>>>>> starting thread");
            waiter.start();

            nodeService.notifyTransport(managerCredentials, shipperCredentials.getAddress(), lastDeployedContract.getContractAddress());
            logger.info(">>>>>> send Transport notification");

            waiter.join();
            logger.info(">>>>>> thread is dead");

            subscription.unsubscribe();
            logger.info(">>>>>> unsubscribed");

            verifyContractInitialState(nodeService.loadTransportContract(managerCredentials, transportObserver.getContractAddress()));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTransportEvents() {
        TransportObserverImpl transportObserver = new TransportObserverImpl(shipperCredentials.getAddress());
        Subscription subscription = nodeService.registerForTransportEvents(transportObserver);

        try {
            Thread waiter = new Thread(() -> {
                synchronized (transportObserver) {
                    try {
                        logger.info(">>>>>> in thread: wait for transport created event");
                        transportObserver.wait();

                        logger.info(">>>>>> in thread: caught transport created event");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            logger.info(">>>>>> starting thread");
            waiter.start();

            byte[] symmetricKey1 = new byte[32];
            byte[] symmetricKey2 = new byte[32];
            byte[] symmetricKey3 = new byte[32];
            List<byte[]> keys = Arrays.asList(symmetricKey1, symmetricKey2, symmetricKey3);

            lastDeployedContract.submitDocument("My Document", "My document URL", BigInteger.ZERO, Arrays.asList(shipperCredentials.getAddress()), keys);

            logger.info(">>>>>> send Transport notification");

            waiter.join();
            logger.info(">>>>>> thread is dead");

            subscription.unsubscribe();
            logger.info(">>>>>> unsubscribed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOfflineDeployContract() {
        try {
            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
            BigInteger nonce = nodeService.getNonce(managerCredentials.getAddress());

            RawTransaction deployTx = nodeService.buildDeployTx(
                    shipperCredentials.getAddress(), receiverCredentials.getAddress(), msecSinceEpoc, nonce, GAS_PRICE, GAS_LIMIT
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
//package com.olam.node.service.infrastructure;
//
//import com.olam.node.service.infrastructure.blockchain.EthereumNodeServiceImpl;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.web3j.crypto.RawTransaction;
//import org.web3j.tuples.generated.Tuple4;
//
//import java.math.BigInteger;
//import java.util.*;
//import java.util.concurrent.ExecutionException;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotNull;
//
//
//public class EthereumNodeServiceImplTest extends OfflineEthereumServiceImplTest {
//    static private EthereumNodeServiceImpl nodeService = null;
//
//    @BeforeClass
//    public static void setup() {
//        try {
//            OfflineEthereumServiceImplTest.setup();
//
//            nodeService = new EthereumNodeServiceImpl(properties.getProperty("rpcurl.rinkeby.eli"));
//
//            if (nodeService.getEtherBalance(shipperCredentials.getAddress()) < 0.25) {
//                nodeService.sendEther(managerCredentials, shipperCredentials.getAddress(), 0.5f);
//            }
//            if (nodeService.getEtherBalance(receiverCredentials.getAddress()) < 0.25) {
//                nodeService.sendEther(managerCredentials, receiverCredentials.getAddress(), 0.5f);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // < online
//    @Test
//    public void testLoadContract() {
//        try {
//            // deploy
//            Transport contract = deployTransportContract();
//            assertNotNull(contract);
//            assertFalse(contract.getContractAddress().isEmpty());
//
//            // load
//            contract = loadTransportContract(contract.getContractAddress());
//            assertNotNull(contract);
//
//            verifyContractInitialState(contract);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testSubmitRequestDoc() {
//        try {
//            // load
//            Transport contract = deployTransportContract();
//            assert(contract != null);
//
//            Transport transportContract = nodeService.loadTransportContract(managerCredentials, contract.getContractAddress());
//
//            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
//            List<String> recipients = Arrays.asList(managerCredentials.getAddress(), shipperCredentials.getAddress(), receiverCredentials.getAddress());
//
//            byte[] symmetricKey1 = new byte[32];
//            byte[] symmetricKey2 = new byte[32];
//            byte[] symmetricKey3 = new byte[32];
//            List<byte[]> keys = Arrays.asList(symmetricKey1, symmetricKey2, symmetricKey3);
//
//            transportContract.submitDocument("AirwayBill", "AirwayBill hash from IPFS", BigInteger.valueOf(msecSinceEpoc), recipients, keys).send();
//            Tuple4<String, BigInteger, String, BigInteger/*, byte[]*/> document = transportContract.requestDocument("AirwayBill").send();
//
//            assert (document.getValue1().equals("AirwayBill hash from IPFS"));      // verify url
//            assert (document.getValue2().intValue() == 0);                          // verify version
//            assert (document.getValue3().equals(managerCredentials.getAddress()));  // verify submitter address
//            assert (document.getValue4().longValue() == msecSinceEpoc);             // verify timestamp
//            //assert (document.getValue5()[0] == symmetricKey1[0]);                   // verify symmetric key
//
//            transportContract.submitDocument("AirwayBill", "AirwayBill hash from IPFS", BigInteger.valueOf(msecSinceEpoc), recipients, keys).send();
//            document = transportContract.requestDocument("AirwayBill").send();
//
//            assert (document.getValue1().equals("AirwayBill hash from IPFS"));      // verify url
//            assert (document.getValue2().intValue() == 1);                          // verify version
//            assert (document.getValue3().equals(managerCredentials.getAddress()));  // verify submitter address
//            assert (document.getValue4().longValue() == msecSinceEpoc);             // verify timestamp
//            //assert (document.getValue5()[0] == symmetricKey1[0]);                   // verify symmetric key
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testSendDeployTx() {
//        try {
//            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
//            RawTransaction deployTransaction = nodeService.buildDeployTx(
//                    managerCredentials.getAddress(), shipperCredentials.getAddress(), receiverCredentials.getAddress(), msecSinceEpoc
//            );
//
//            assertNotNull(deployTransaction);
//
//            String signedTransaction = nodeService.signTransaction(deployTransaction, managerCredentials);
//            assertNotNull(signedTransaction);
//            assertFalse(signedTransaction.isEmpty());
//
//            String contractAddress = nodeService.sendDeployTx(signedTransaction);
//            assertNotNull(contractAddress);
//            assertFalse(contractAddress.isEmpty());
//
//            Transport contract = loadTransportContract(contractAddress);
//            assertNotNull(contract);
//
//            verifyContractInitialState(contract);
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testSendSubmitDocTx() {
//        final String DOC_NAME = "Payment request";
//
//        try {
//            String contractAddress = deployTransportContract().getContractAddress();
//
//            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
//            List<String> recipientsAddresses = Arrays.asList(shipperCredentials.getAddress(), receiverCredentials.getAddress());
//            List<byte[]> keys = new ArrayList<>();
//
//            byte[] key1 = new byte[32];
//            byte[] key2 = new byte[32];
//
//            keys.add(key1);
//            keys.add(key2);
//
//            for (int i = 0 ; i < 2 ; i++) {
//                RawTransaction submitDcoTx = nodeService.buildSubmitDocTx(
//                        managerCredentials.getAddress(), contractAddress, DOC_NAME, "some IPFS shit-hash", recipientsAddresses, keys, msecSinceEpoc
//                );
//
//                assertNotNull(submitDcoTx);
//
//                String signedTransaction = nodeService.signTransaction(submitDcoTx, managerCredentials);
//                assertNotNull(signedTransaction);
//                assertFalse(signedTransaction.isEmpty());
//
//                nodeService.sendSubmitDocTx(signedTransaction);
//
//                Transport contract = nodeService.loadTransportContract(managerCredentials, contractAddress);
//
//                Tuple4<String, BigInteger, String, BigInteger> document = contract.requestDocument(DOC_NAME).sendAsync().get();
//
//                assert (document.getValue1().equals("AirwayBill hash from IPFS"));      // verify url
//                assert (document.getValue2().intValue() == 1);                          // verify version
//                assert (document.getValue3().equals(managerCredentials.getAddress()));  // verify submitter address
//                assert (document.getValue4().longValue() == msecSinceEpoc);             // verify timestamp
//                //assert (document.getValue5()[0] == key1[0]);                            // verify symmetric key
//                //assert (document.getValue5()[1] == key1[1]);                            // verify symmetric key
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testSendRequestDocTx() {
//        final String DOC_NAME = "Payment request";
//        final String DOC_URL = "some IPFS shit-hash";
//
//
//        try {
//            String contractAddress = deployTransportContract().getContractAddress();
//
//            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
//            List<String> recipientsAddresses = Arrays.asList(shipperCredentials.getAddress(), receiverCredentials.getAddress());
//            List<byte[]> keys = new ArrayList<>();
//
//            byte[] key1 = new byte[32];
//            byte[] key2 = new byte[32];
//
//            keys.add(key1);
//            keys.add(key2);
//
//            for (int i = 0 ; i < 2 ; i++) {
//                // online tx
//                nodeService.submitDocument(managerCredentials, contractAddress, DOC_NAME, DOC_URL);
//
//                Transport contract = nodeService.loadTransportContract(managerCredentials, contractAddress);
//
//                Tuple4<String, BigInteger, String, BigInteger> document = contract.requestDocument(DOC_NAME).sendAsync().get();
//
//                assert (document.getValue1().equals("AirwayBill hash from IPFS"));      // verify url
//                assert (document.getValue2().intValue() == 1);                          // verify version
//                assert (document.getValue3().equals(managerCredentials.getAddress()));  // verify submitter address
//                assert (document.getValue4().longValue() == msecSinceEpoc);             // verify timestamp
//                //assert (document.getValue5()[0] == key1[0]);                            // verify symmetric key
//                //assert (document.getValue5()[1] == key1[1]);                            // verify symmetric key
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static Transport deployTransportContract() {
//        long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
//        Transport contract = null;
//
//        try {
//            contract = nodeService.deployTransportContract(managerCredentials, shipperCredentials.getAddress(), receiverCredentials.getAddress(), msecSinceEpoc);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return contract;
//    }
//
//    private static Transport loadTransportContract(String contractAddress) {
//        return nodeService.loadTransportContract(managerCredentials, contractAddress);
//    }
//
//    private void verifyContractInitialState(Transport contract) {
//        try {
//            // verify state
//            assert (contract.getState().send().equals("none"));
//
//            // verify address => role
//            assert (contract.getRole(managerCredentials.getAddress()).send().equals("manager"));
//            assert (contract.getRole(shipperCredentials.getAddress()).send().equals("shipper"));
//            assert (contract.getRole(receiverCredentials.getAddress()).send().equals("receiver"));
//
//            // verify role => address
//            assert (contract.getAddress("manager").send().equals(managerCredentials.getAddress()));
//            assert (contract.getAddress("shipper").send().equals(shipperCredentials.getAddress()));
//            assert (contract.getAddress("receiver").send().equals(receiverCredentials.getAddress()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
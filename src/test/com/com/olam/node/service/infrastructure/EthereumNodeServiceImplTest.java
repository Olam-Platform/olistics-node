package com.olam.node.service.infrastructure;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.RawTransaction;
import org.web3j.tuples.generated.Tuple5;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


public class EthereumNodeServiceImplTest extends OfflineEthereumServiceImplTest {
    static private EthereumNodeServiceImpl nodeService = null;

    @BeforeClass
    public static void setup() {
        try {
            OfflineEthereumServiceImplTest.setup();

            nodeService = new EthereumNodeServiceImpl(properties.getProperty("rpcurl.rinkeby.eli"));

            if (nodeService.getEtherBalance(shipperCredentials.getAddress()) < 0.25) {
                nodeService.sendEther(managerCredentials, shipperCredentials.getAddress(), 0.5f);
            }
            if (nodeService.getEtherBalance(receiverCredentials.getAddress()) < 0.25) {
                nodeService.sendEther(managerCredentials, receiverCredentials.getAddress(), 0.5f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadContract() {
        try {
            // deploy
            Transport contract = deployTransportContract();
            assert(contract != null);
            assert(!contract.getContractAddress().isEmpty());

            // load
            contract = loadTransportContract(contract.getContractAddress());
            assert(contract != null);

            verifyContractInitialState(contract);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDocSubmitRequest() {
        try {
            // load
            Transport contract = deployTransportContract();
            assert(contract != null);

            Transport transportContract = nodeService.loadTransportContract(managerCredentials, contract.getContractAddress());

            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
            List<String> recipients = Arrays.asList(managerCredentials.getAddress(), shipperCredentials.getAddress(), receiverCredentials.getAddress());

            byte[] symmetricKey1 = new byte[32];
            byte[] symmetricKey2 = new byte[32];
            byte[] symmetricKey3 = new byte[32];
            List<byte[]> keys = Arrays.asList(symmetricKey1, symmetricKey2, symmetricKey3);

            transportContract.submitDocument("AirwayBill", "AirwayBill hash from IPFS", BigInteger.valueOf(msecSinceEpoc), recipients, keys).send();
            Tuple5<String, BigInteger, String, BigInteger, byte[]> document = transportContract.requestDocument("AirwayBill").sendAsync().get();

            assert (document.getValue1().equals("AirwayBill hash from IPFS"));      // verify url
            assert (document.getValue2().intValue() == 0);                          // verify version
            assert (document.getValue3().equals(managerCredentials.getAddress()));  // verify submitter address
            assert (document.getValue4().longValue() == msecSinceEpoc);             // verify timestamp
            assert (document.getValue5()[0] == symmetricKey1[0]);                   // verify symmetric key

            transportContract.submitDocument("AirwayBill", "AirwayBill hash from IPFS", BigInteger.valueOf(msecSinceEpoc), recipients, keys).send();
            document = transportContract.requestDocument("AirwayBill").sendAsync().get();

            assert (document.getValue1().equals("AirwayBill hash from IPFS"));      // verify url
            assert (document.getValue2().intValue() == 1);                          // verify version
            assert (document.getValue3().equals(managerCredentials.getAddress()));  // verify submitter address
            assert (document.getValue4().longValue() == msecSinceEpoc);             // verify timestamp
            assert (document.getValue5()[0] == symmetricKey1[0]);                   // verify symmetric key
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSendDeployTransaction() {
        try {
            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
            RawTransaction deployTransaction = nodeService.buildDeployTx(
                    managerCredentials.getAddress(), shipperCredentials.getAddress(), receiverCredentials.getAddress(), msecSinceEpoc
            );

            assertNotNull(deployTransaction);

            String signedTransaction = nodeService.signTransaction(deployTransaction, managerCredentials);
            assertNotNull(signedTransaction);
            assertFalse(signedTransaction.isEmpty());

            String contractAddress = nodeService.sendDeployTransaction(signedTransaction);
            assertNotNull(contractAddress);
            assertFalse(contractAddress.isEmpty());

            Transport contract = loadTransportContract(contractAddress);
            assertNotNull(contract);

            verifyContractInitialState(contract);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static Transport deployTransportContract() {
        long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
        Transport contract = null;

        try {
            contract = nodeService.deployTransportContract(managerCredentials, shipperCredentials.getAddress(), receiverCredentials.getAddress(), msecSinceEpoc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contract;
    }

    private static Transport loadTransportContract(String contractAddress) {
        return nodeService.loadTransportContract(managerCredentials, contractAddress);
    }

    private void verifyContractInitialState(Transport contract) {
        try {
            // verify state
            assert (contract.GetState().send().equals("none"));

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
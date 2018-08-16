package com.olam.node.service;

import com.olam.node.sdk.Transport;
import com.olam.node.service.infrastructure.EthereumNodeService;
import com.olam.node.service.infrastructure.EthereumNodeServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.util.ResourceUtils;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.tuples.generated.Tuple5;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;


public class TestBasicContractFunctionality {
    static final byte[] symmetricKey1 = new byte[32];
    static final byte[] symmetricKey2 = new byte[32];
    static final byte[] symmetricKey3 = new byte[32];

    static private EthereumNodeService nodeService = null;
    static private Properties properties = new Properties();

    private static List<Credentials> credentials = new ArrayList<>();
    private static String transportContractAddress = null;
    static Credentials managerCredentials;
    static Credentials shipperCredentials;
    static Credentials receiverCredentials;

    Logger logger = getLogger(getClass().getName());

    @BeforeClass
    public static void setUp() {
        try {
            properties.load(new FileReader(ResourceUtils.getFile("classpath:application.properties")));

            nodeService = new EthereumNodeServiceImpl(properties.getProperty("rpcurl.rinkeby"));

            LoadCredentials();

            managerCredentials = credentials.get(Integer.parseInt(properties.getProperty("account_id.manager")));
            shipperCredentials = credentials.get(Integer.parseInt(properties.getProperty("account_id.shipper")));
            receiverCredentials = credentials.get(Integer.parseInt(properties.getProperty("account_id.receiver")));

            symmetricKey1[0] = 0x11;
            symmetricKey2[0] = 0x22;
            symmetricKey3[0] = 0x33;

            if (nodeService.GetEtherBalance(shipperCredentials.getAddress()) < 0.25) {
                nodeService.SendEther(managerCredentials, shipperCredentials.getAddress(), 0.5f);
            }

            if (nodeService.GetEtherBalance(receiverCredentials.getAddress()) < 0.25) {
                nodeService.SendEther(managerCredentials, receiverCredentials.getAddress(), 0.5f);
            }

            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();

            transportContractAddress = nodeService.DeployTransportContract(
                    managerCredentials, shipperCredentials.getAddress(), receiverCredentials.getAddress(), msecSinceEpoc
            ).getContractAddress();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadContract() {
        try {
            assert(transportContractAddress != null);

            // load
            Transport transportContract = nodeService.LoadTransportContract(managerCredentials, transportContractAddress);

            // verify state
            assert(transportContract.GetState().send().equals("none"));

            // verify address => role
            assert(transportContract.getRole(managerCredentials.getAddress()).send().equals("manager"));
            assert(transportContract.getRole(shipperCredentials.getAddress()).send().equals("shipper"));
            assert(transportContract.getRole(receiverCredentials.getAddress()).send().equals("receiver"));

            // verify role => address
            assert(transportContract.getAddress("manager").send().equals(managerCredentials.getAddress()));
            assert(transportContract.getAddress("shipper").send().equals(shipperCredentials.getAddress()));
            assert(transportContract.getAddress("receiver").send().equals(receiverCredentials.getAddress()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSubmitDocument() {
        try {
            // load
            Transport transportContract = nodeService.LoadTransportContract(managerCredentials, transportContractAddress);

            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
            List<String> recipients = Arrays.asList(managerCredentials.getAddress(), shipperCredentials.getAddress(), receiverCredentials.getAddress());

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void LoadCredentials() {
        try {
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:accounts/keystore_file01.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:accounts/keystore_file02.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:accounts/keystore_file03.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:accounts/keystore_file04.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:accounts/keystore_file05.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:accounts/keystore_file06.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:accounts/keystore_file07.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:accounts/keystore_file08.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:accounts/keystore_file09.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:accounts/keystore_file10.json")));
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
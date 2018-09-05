package com.olam.node.service.infrastructure;

import com.olam.node.service.infrastructure.blockchain.OfflineEthereumServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.WalletUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


public class OfflineEthereumServiceImplTest {
    static private OfflineEthereumServiceImpl nodeService = null;
    static protected String RPC_URL = "rpcurl.ganache";

    static Properties properties = new Properties();

    private static List<Credentials> credentials = new ArrayList<>();
    static Credentials managerCredentials;
    static Credentials shipperCredentials;
    static Credentials receiverCredentials;

    protected Transport lastDeployedContract = null;

    //Logger logger = getLogger(getClass().getName());

    @BeforeClass
    public static void setup() {
        try {
            properties.load(new FileReader(ResourceUtils.getFile("classpath:application.properties")));

            nodeService = new OfflineEthereumServiceImpl(properties.getProperty(RPC_URL));

            if (RPC_URL.equals("rpcurl.rinkeby.eli")) {
                loadTestnetCredentials();
            } else if (RPC_URL.equals("rpcurl.ganache")) {
                loadGanacheCredentials();
            }

            managerCredentials = credentials.get(Integer.parseInt(properties.getProperty("account_id.manager")));
            shipperCredentials = credentials.get(Integer.parseInt(properties.getProperty("account_id.shipper")));
            receiverCredentials = credentials.get(Integer.parseInt(properties.getProperty("account_id.receiver")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuildDeployTx() {
        RawTransaction deployTx = buildDeployTx(managerCredentials.getAddress(), shipperCredentials.getAddress(), receiverCredentials.getAddress());

        assertNotNull(deployTx);
    }

    @Test
    public void testBuildSubmitDocTx() {
        List<String> recipientsAddresses = Arrays.asList(shipperCredentials.getAddress(), receiverCredentials.getAddress());
        List<byte[]> keys = new ArrayList<>();

        byte[] key1 = new byte[32];
        byte[] key2 = new byte[32];

        keys.add(key1);
        keys.add(key2);

        RawTransaction tx = buildSubmitDocTx(
                managerCredentials.getAddress(), "", "docName", "docUrl", recipientsAddresses, keys
        );

        assertNotNull(tx);
    }

    @Test
    public void testSignTx() {
        RawTransaction deployTransaction = buildDeployTx(
                managerCredentials.getAddress(), shipperCredentials.getAddress(), receiverCredentials.getAddress()
        );

        assert(deployTransaction != null);

        String signedTransaction = nodeService.signTransaction(deployTransaction, managerCredentials);

        assertNotNull(signedTransaction);
        assertFalse(signedTransaction.isEmpty());
    }

    //region helpers
    private static void loadTestnetCredentials() {
        try {
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file01.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file02.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file03.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file04.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file05.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file06.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file07.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file08.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file09.json")));
            credentials.add(WalletUtils.loadCredentials("", ResourceUtils.getFile("classpath:keystore_files/keystore_file10.json")));
        } catch (CipherException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadGanacheCredentials() {
        credentials.add(Credentials.create(properties.getProperty("ganache.privatekey01")));
        credentials.add(Credentials.create(properties.getProperty("ganache.privatekey02")));
        credentials.add(Credentials.create(properties.getProperty("ganache.privatekey03")));
        credentials.add(Credentials.create(properties.getProperty("ganache.privatekey04")));
        credentials.add(Credentials.create(properties.getProperty("ganache.privatekey05")));
    }
    private RawTransaction buildDeployTx(String fromAddress, String shipperAddress, String receiverAddress) {
        RawTransaction rawTransaction = null;

        try {
            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
            rawTransaction = nodeService.buildDeployTx(fromAddress, shipperAddress, receiverAddress , msecSinceEpoc);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        assertNotNull(rawTransaction);
        return rawTransaction;
    }

    private RawTransaction buildSubmitDocTx(String fromAddress, String contractAddress, String docName, String docUrl, List<String> recipients, List<byte[]> keys) {
        RawTransaction tx = null;

        try {
            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
            tx = nodeService.buildSubmitDocTx(fromAddress, contractAddress, docName , docUrl, recipients, keys, msecSinceEpoc);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        assertNotNull(tx);

        return tx;
    }
    //endregion
}
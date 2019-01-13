package com.olam.node.integration;

import com.olam.node.service.infrastructure.blockchain.OfflineEthereumService;
import com.olam.node.service.infrastructure.blockchain.ShipmentContract;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.WalletUtils;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


public class OfflineEthereumServiceTest {
    BigInteger nonce = BigInteger.ZERO;

    static String RPC_URL;
    static BigInteger GAS_PRICE;
    static BigInteger GAS_LIMIT;
    static String NODE_USR;
    static String NODE_PASS;

    static private OfflineEthereumService nodeService = null;

    static Properties properties = new Properties();

    private static List<Credentials> credentials = new ArrayList<>();
    static Credentials managerCredentials;
    static Credentials shipperCredentials;
    static Credentials receiverCredentials;

    protected ShipmentContract lastDeployedContract = null;

    @BeforeClass
    public static void setup() {
        try {
            properties.load(new FileReader(ResourceUtils.getFile("classpath:application.properties")));

            RPC_URL   = properties.getProperty("aws.rpcurl.ws");
            GAS_PRICE = BigInteger.valueOf(Long.parseLong(properties.getProperty("aws.gasprice")));
            GAS_LIMIT = BigInteger.valueOf(Long.parseLong(properties.getProperty("aws.gaslimit.deploy")));
            NODE_USR = properties.getProperty("kaleido.usr");
            NODE_PASS = properties.getProperty("kaleido.pass");

            boolean useGanacheCredentials = false;

            loadTestCredentials();

            managerCredentials = credentials.get(Integer.parseInt(properties.getProperty("account_id.manager")));
            shipperCredentials = credentials.get(Integer.parseInt(properties.getProperty("account_id.shipper")));
            receiverCredentials = credentials.get(Integer.parseInt(properties.getProperty("account_id.receiver")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBuildDeployTx() {
        RawTransaction deployTx = buildDeployTx(
                shipperCredentials.getAddress(), receiverCredentials.getAddress(), nonce, GAS_PRICE, GAS_LIMIT
        );

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
                managerCredentials.getAddress(), "", "docName", "docUrl", recipientsAddresses, keys,
                nonce, GAS_PRICE, GAS_LIMIT
        );

        assertNotNull(tx);
    }

    @Test
    public void testSignTx() {
        RawTransaction deployTransaction = buildDeployTx(
                shipperCredentials.getAddress(), receiverCredentials.getAddress(), nonce, GAS_PRICE, GAS_LIMIT
        );

        assert(deployTransaction != null);

        String signedTransaction = nodeService.signTransaction(deployTransaction, managerCredentials);

        assertNotNull(signedTransaction);
        assertFalse(signedTransaction.isEmpty());
    }

    //region helpers
    private static void loadTestCredentials() {
        try {
            //rinkeby
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

            // ganache
            credentials.add(Credentials.create(properties.getProperty("ganache.privatekey1")));
            credentials.add(Credentials.create(properties.getProperty("ganache.privatekey2")));
            credentials.add(Credentials.create(properties.getProperty("ganache.privatekey3")));
            credentials.add(Credentials.create(properties.getProperty("ganache.privatekey4")));
            credentials.add(Credentials.create(properties.getProperty("ganache.privatekey5")));

        } catch (CipherException | IOException e) {
            e.printStackTrace();
        }
    }

    private RawTransaction buildDeployTx(String shipperAddress, String receiverAddress, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit)
    {
        RawTransaction rawTransaction = null;

        long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
        rawTransaction = nodeService.buildDeployTx(shipperAddress, receiverAddress , msecSinceEpoc, nonce, gasPrice, gasLimit);


        assertNotNull(rawTransaction);
        return rawTransaction;
    }

    private RawTransaction buildSubmitDocTx(
            String fromAddress, String contractAddress, String docName, String docUrl, List<String> recipients, List<byte[]> keys,
            BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit
    ) {
        RawTransaction tx = null;

        long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
        tx = nodeService.buildSubmitDocTx(contractAddress, docName , docUrl, recipients, keys, msecSinceEpoc, nonce, gasPrice, gasLimit);
        assertNotNull(tx);

        return tx;
    }
    //endregion
}
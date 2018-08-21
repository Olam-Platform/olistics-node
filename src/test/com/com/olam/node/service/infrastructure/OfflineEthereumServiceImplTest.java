package com.olam.node.service.infrastructure;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.WalletUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


public class OfflineEthereumServiceImplTest {
    static private OfflineEthereumServiceImpl nodeService = null;
    static Properties properties = new Properties();

    private static List<Credentials> credentials = new ArrayList<>();
    static Credentials managerCredentials;
    static Credentials shipperCredentials;
    static Credentials receiverCredentials;

    //Logger logger = getLogger(getClass().getName());

    @BeforeClass
    public static void setup() {
        try {
            properties.load(new FileReader(ResourceUtils.getFile("classpath:application.properties")));

            nodeService = new OfflineEthereumServiceImpl(properties.getProperty("rpcurl.rinkeby.eli"));

            loadCredentials();

            managerCredentials = credentials.get(Integer.parseInt(properties.getProperty("account_id.manager")));
            shipperCredentials = credentials.get(Integer.parseInt(properties.getProperty("account_id.shipper")));
            receiverCredentials = credentials.get(Integer.parseInt(properties.getProperty("account_id.receiver")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateDeployTransaction() {
        RawTransaction deployTransaction = createDeployTransaction(
                managerCredentials.getAddress(), shipperCredentials.getAddress(), receiverCredentials.getAddress()
        );

        assert(deployTransaction != null);
    }

    @Test
    public void testSignTransaction() {
        RawTransaction deployTransaction = createDeployTransaction(
                managerCredentials.getAddress(), shipperCredentials.getAddress(), receiverCredentials.getAddress()
        );

        assert(deployTransaction != null);

        String signedTransaction = nodeService.signTransaction(deployTransaction, managerCredentials);

        assertNotNull(signedTransaction);
        assertFalse(signedTransaction.isEmpty());
    }

    private static void loadCredentials() {
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
        } catch (CipherException | IOException e) {
            e.printStackTrace();
        }
    }

    private RawTransaction createDeployTransaction(String deployerAddress, String shipperAddress, String receiverAddress) {
        RawTransaction rawTransaction = null;

        try {
            long msecSinceEpoc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
            rawTransaction = nodeService.buildDeployTx(deployerAddress, shipperAddress, receiverAddress , msecSinceEpoc);

            assert(rawTransaction != null);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return rawTransaction;
    }
}
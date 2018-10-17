package com.olam.node.unit;

import com.olam.node.sdk.ShipmentImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.FileReader;
import java.util.Properties;

public class ShipmentImplTest {



    private ShipmentImpl shipment;
    private Properties properties = new Properties();



    @Before
    public void setUp() throws Exception {
        properties.load(new FileReader(ResourceUtils.getFile("classpath:application.properties")));
        String rpcUrl = properties.getProperty("infura.rinkeby.rpcurl.http");
        shipment = new ShipmentImpl(rpcUrl);
    }

    @Test
    public void createShipment() throws Exception {
        String wallet = WalletUtils.generateLightNewWalletFile("", null);
        Credentials credentials = WalletUtils.loadCredentials("", wallet);
        String shipmentCreationSignedTrx = this.shipment.createShipment(credentials,
                "0x28Ac189AA5B4Df9349D55B18622548ae27A58D8a",
                "0xAecf24415c676ae06DE40DD627c40559aF9C6FAb",
                "0xED09F036B6Ee0B4177B1e91D69036bC85a357635");
        shipmentCreationSignedTrx.toString();
    }
}
package com.olam.node.service.application;

import org.junit.Before;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

public class EthereumCryptoServiceTest {

    EthereumCryptoService cryptoService = new EthereumCryptoService();

    Credentials credentials;

    @Before
    public void setUp() throws Exception {
        File wallet = new File("./");
        String source = WalletUtils.generateFullNewWalletFile("", wallet);
        credentials = WalletUtils.loadCredentials("", source);
        Files.deleteIfExists(Paths.get(source));
    }

    @Test
    public void encryptAsymetricKey() throws GeneralSecurityException {
        SecretKeySpec aes = cryptoService.generateSymmetricKey(16, "AES");
        byte[] pubKey = credentials.getEcKeyPair().getPublicKey().toByteArray();
        byte[] encryptedPublicKey = cryptoService.encryptECPublicKey(
                pubKey, aes, "ECIES");
        System.out.println(encryptedPublicKey.length);
    }
}
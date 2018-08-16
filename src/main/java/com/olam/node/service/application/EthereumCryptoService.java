package com.olam.node.service.application;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.SecureRandom;

public class EthereumCryptoService implements CryptoService {

    private Cipher cipher;

    @Override
    public SecretKeySpec generateSymmetricKey(int length, String algorithm) {
        SecureRandom rnd = new SecureRandom();
        byte[] key = new byte[length];
        rnd.nextBytes(key);
        return new SecretKeySpec(key, algorithm);
    }

    @Override
    public byte[] encryptData(byte[] data, SecretKeySpec symmetricKey, String cipherAlgorithm) {
        return new byte[0];
    }

    @Override
    public byte[] encryptKey(PublicKey keyToEncrypt, SecretKeySpec symmetricKey, String cipherAlgorithm)
            throws GeneralSecurityException {
        this.cipher = Cipher.getInstance(cipherAlgorithm);
        this.cipher.init(Cipher.ENCRYPT_MODE, keyToEncrypt);
        return this.cipher.doFinal(symmetricKey.getEncoded());
    }



}

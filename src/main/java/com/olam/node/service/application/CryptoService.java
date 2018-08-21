package com.olam.node.service.application;

import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.PublicKey;

public interface CryptoService {
    SecretKeySpec generateSymmetricKey(int length, String algorithm);

    byte[] encryptData(byte[] data, SecretKeySpec symmetricKey, String cipherAlgorithm);

    byte[] encryptKey(PublicKey keyToEncrypt, SecretKeySpec symmetricKey, String cipherAlgorithm) throws GeneralSecurityException;
}

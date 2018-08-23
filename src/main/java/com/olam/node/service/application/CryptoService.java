package com.olam.node.service.application;

import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.PublicKey;

public interface CryptoService {

    SecretKeySpec generateSymmetricKey(int length, String algorithm);

    byte[] encryptData(byte[] data, SecretKeySpec symmetricKey, String cipherAlgorithm) throws InvalidKeyException, GeneralSecurityException;

    byte[] encryptKey(PublicKey keyToEncrypt, SecretKeySpec symmetricKey, String cipherAlgorithm) throws GeneralSecurityException;

//    byte[] encryptECPublicKey(String keyToEncrypt, SecretKeySpec symmetricKey, String cipherAlgorithm) throws GeneralSecurityException;

    byte[] encryptECPublicKey(byte[] keyToEncrypt, SecretKeySpec symmetricKey, String cipherAlgorithm) throws GeneralSecurityException;

}

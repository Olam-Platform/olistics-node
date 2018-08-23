package com.olam.node.service.application;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

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
    public byte[] encryptData(byte[] data, SecretKeySpec symmetricKey, String cipherAlgorithm) throws GeneralSecurityException {
        this.cipher = Cipher.getInstance(cipherAlgorithm);
        this.cipher.init(Cipher.ENCRYPT_MODE, symmetricKey);
        byte[] encryptedData = this.cipher.doFinal(data);
        return encryptedData;
    }

    @Override
    public byte[] encryptKey(PublicKey publicKey, SecretKeySpec symmetricKeyToEncrypt, String cipherAlgorithm)
            throws GeneralSecurityException {

        this.cipher = Cipher.getInstance(cipherAlgorithm, "BC");
        this.cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return this.cipher.doFinal(symmetricKeyToEncrypt.getEncoded());
    }

    @Override
    public byte[] encryptECPublicKey(byte[] keyToEncrypt, SecretKeySpec symmetricKey, String cipherAlgorithm) throws GeneralSecurityException {
        PublicKey publicKey = this.convertBytesToPublicKey(keyToEncrypt);
//        PublicKey publicKey = this.getPublicKeyFromBytes(keyToEncrypt);
//        ECPublicKey publicKey = getPublicKey(keyToEncrypt);
//        PublicKey publicKey = convertP256Key(keyToEncrypt);

        return this.encryptKey(publicKey, symmetricKey, cipherAlgorithm);
    }

//    @Override
//    public byte[] encryptECPublicKey(String keyToEncrypt, SecretKeySpec symmetricKey, String cipherAlgorithm) throws GeneralSecurityException {
//        PublicKey publicKey = this.convertBytesToPublicKey(keyToEncrypt);
//        return this.encryptKey(publicKey, symmetricKey, cipherAlgorithm);
//    }


    private PublicKey convertBytesToPublicKey(byte[] publicKey) throws NoSuchProviderException, NoSuchAlgorithmException,
            InvalidKeySpecException {

        KeyFactory factory = KeyFactory.getInstance("EC");
        PublicKey ecPublicKey = (ECPublicKey) factory.generatePublic(new X509EncodedKeySpec(publicKey));
        return ecPublicKey;
    }
//
//    private PublicKey getPublicKeyFromBytes(byte[] pubKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
//         ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("prime256v1");
//        KeyFactory kf = KeyFactory.getInstance("ECDSA", new BouncyCastleProvider());
//        ECNamedCurveSpec params = new ECNamedCurveSpec("prime256v1", spec.getCurve(), spec.getG(), spec.getN());
//        ECPoint point = ECPointUtil.decodePoint(params.getCurve(),pubKey);
//        ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(point, params);
//        ECPublicKey pk = (ECPublicKey) kf.generatePublic(pubKeySpec);
//        return pk;
//    }


//    public static ECPublicKey getPublicKey(byte[] publicKeyBytes) {
//        // First we separate x and y of coordinates into separate variables
//        byte[] x = new byte[32];
//        byte[] y = new byte[32];
//        System.arraycopy(publicKeyBytes, 1, x, 0, 32);
//        System.arraycopy(publicKeyBytes, 33, y, 0, 32);
//
//        try {
//            KeyFactory kf = KeyFactory.getInstance("EC");
//
//            AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
//            parameters.init(new ECGenParameterSpec("secp256r1"));
//            ECParameterSpec ecParameterSpec = parameters.getParameterSpec(ECParameterSpec.class);
//
//            ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(new ECPoint(new BigInteger(x), new BigInteger(y)), ecParameterSpec);
//            ECPublicKey ecPublicKey = (ECPublicKey) kf.generatePublic(ecPublicKeySpec);
//            return ecPublicKey;
//        } catch (NoSuchAlgorithmException | InvalidParameterSpecException | InvalidKeySpecException e) {
//            System.out.println(e.getMessage());
//            return null;
//        }
//    }


//    public static ECPublicKey convertP256Key(byte[] w) throws InvalidKeySpecException {
//        byte[] encodedKey = new byte[P256_HEAD.length + w.length];
//        System.arraycopy(P256_HEAD, 0, encodedKey, 0, P256_HEAD.length);
//        System.arraycopy(w, 0, encodedKey, P256_HEAD.length, w.length);
//        KeyFactory eckf;
//        try {
//            eckf = KeyFactory.getInstance("EC");
//        } catch (NoSuchAlgorithmException e) {
//            throw new IllegalStateException("EC key factory not present in runtime");
//        }
//        X509EncodedKeySpec ecpks = new X509EncodedKeySpec(encodedKey);
//        return (ECPublicKey) eckf.generatePublic(ecpks);
//    }
//
//    private static byte[] P256_HEAD = Base64.decode("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE");

}

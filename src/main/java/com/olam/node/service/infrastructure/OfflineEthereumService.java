package com.olam.node.service.infrastructure;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;

import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface OfflineEthereumService {
    Credentials loadCredentials(File keystoreFile, String password) throws IOException, CipherException;

    RawTransaction buildDeployTx(String deployerAddress, String shipperAddress, String receiverAddress, long timeStamp) throws ExecutionException, InterruptedException;

    // require: length of byte[] key should be 32
    //          recipients.length() == keyList.length()
    RawTransaction buildSubmitDocTx(String docName, String docUrl, List<String> recipients, List<byte[]> keyList) throws ExecutionException, InterruptedException;

    RawTransaction buildRequestDocTx(String docName) throws ExecutionException, InterruptedException;

    RawTransaction buildRequestDocTx(String docName, int docVersion) throws ExecutionException, InterruptedException;

    String signTransaction(RawTransaction transaction, Credentials credentials);
}

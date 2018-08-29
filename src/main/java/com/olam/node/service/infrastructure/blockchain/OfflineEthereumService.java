package com.olam.node.service.infrastructure.blockchain;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface OfflineEthereumService {
    Credentials loadCredentials(File keystoreFile, String password) throws IOException, CipherException;

    Credentials loadCredentials(String keystoreFilePath, String password) throws IOException, CipherException;

    String signTransaction(RawTransaction transaction, Credentials credentials);
}

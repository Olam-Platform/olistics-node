package com.olam.node.service.infrastructure.blockchain;

import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;


public class OfflineEthereumServiceImpl extends GenericEthereumNode implements OfflineEthereumService {
    public OfflineEthereumServiceImpl(String rpcUrl) {
        super(rpcUrl);

        assert (web3j != null);
    }

    @Override
    public Credentials loadCredentials(File keystoreFile, String password) throws IOException, CipherException {
        return WalletUtils.loadCredentials(password, keystoreFile);
    }

    @Override
    public Credentials loadCredentials(String keystoreFilePath, String password) throws IOException, CipherException {
        return WalletUtils.loadCredentials(password, new File(keystoreFilePath));
    }


    @Override
    public String signTransaction(RawTransaction rawTransaction, Credentials credentials) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Numeric.toHexString(signedMessage);
    }
}

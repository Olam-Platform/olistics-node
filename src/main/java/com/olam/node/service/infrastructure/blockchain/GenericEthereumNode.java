package com.olam.node.service.infrastructure.blockchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

// Used just for building the web3j objects for accessing ethereum and for storing the rpc URL for accessing an ethereum node
public class GenericEthereumNode {

    private Logger logger = LoggerFactory.getLogger(GenericEthereumNode.class);
    protected Web3j web3j;
    protected Credentials credentials;

    public BigInteger gasPrice = BigInteger.valueOf(220000000L);
    public BigInteger gasLimit = BigInteger.valueOf(4300000);

    public final String RPC_URL;

    public GenericEthereumNode(String rpcUrl) {

        this.RPC_URL = rpcUrl;
        this.web3j = Web3j.build(new HttpService(RPC_URL));

        String file = null;
        try {

            file = WalletUtils.generateLightNewWalletFile("strong password", null);
            credentials = WalletUtils.loadCredentials("strong password", file);
            logger.info("Credentials created: file={}, address={}", file, credentials.getAddress());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

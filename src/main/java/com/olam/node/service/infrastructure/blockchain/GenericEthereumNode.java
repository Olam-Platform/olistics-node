package com.olam.node.service.infrastructure.blockchain;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

// Used just for building the web3j objects for accessing ethereum and for storing the rpc URL for accessing an ethereum node
public class GenericEthereumNode {
    protected Web3j web3j;

    protected final static BigInteger gasPrice = BigInteger.valueOf(20000000000L);
    protected final static BigInteger gasLimit = BigInteger.valueOf(6721975);

    protected final String RPC_URL;

    public GenericEthereumNode(String rpcUrl) {
        this.RPC_URL = rpcUrl;

        web3j = Web3j.build(new HttpService(RPC_URL));
    }
}

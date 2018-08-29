package com.olam.node.service.infrastructure;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

// Used just for building the web3j objects for accessing ethereum and for storing the rpc URL for accessing an ethereum node
public class GenericEthereumNode {
    protected Web3j web3j;

    BigInteger gasPrice = BigInteger.valueOf(220000000L);
    BigInteger gasLimit = BigInteger.valueOf(4300000);

    final String RPC_URL;

    public GenericEthereumNode(String rpcUrl) {
        this.RPC_URL = rpcUrl;

        web3j = Web3j.build(new HttpService(RPC_URL));
    }
}

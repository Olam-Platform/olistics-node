package com.olam.node.configuration;

import com.olam.node.service.infrastructure.blockchain.EthereumNodeService;
import com.olam.node.service.infrastructure.blockchain.IEthereumNodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.net.ConnectException;

@SuppressWarnings("ALL")
@Configuration
public class EthereumNodeConfig {
    // Infura websocket params

    /*
    @Value("${rpcurl.infura.rinkeby.http}")
    private String  rpcUrl;
    @Value("${rinkeby.gasprice}")
    private long    gasPrice;
    @Value("${rinkeby.gaslimit}")
    private long    gasLimit;
    */

    @Value("${rpcurl.ganache.http}")
    private String  rpcUrl;
    @Value("${ganache.gasprice}")
    private long    gasPrice;
    @Value("${ganache.gaslimit}")
    private long    gasLimit;

    @Bean
    public IEthereumNodeService getEthereumNodeService() {
        IEthereumNodeService nodeService = null;

        try {
            nodeService = new EthereumNodeService(rpcUrl, BigInteger.valueOf(gasPrice), BigInteger.valueOf(gasLimit));
        } catch (ConnectException e) {
            e.printStackTrace();
        }

        return nodeService;
    }
}

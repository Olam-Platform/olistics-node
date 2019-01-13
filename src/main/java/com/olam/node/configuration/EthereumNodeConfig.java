package com.olam.node.configuration;

import com.olam.node.service.infrastructure.blockchain.EthereumNodeService;
import com.olam.node.service.infrastructure.blockchain.IEthereumNodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.net.ConnectException;

@Configuration
public class EthereumNodeConfig {
    @Value("${rpcurl.ethereum.ganache}")
    private String rpcUrl;

    private final BigInteger GANACHE_GAS_PRICE = BigInteger.valueOf(20000000000l);
    private final BigInteger GANACHE_GAS_LIMIT = BigInteger.valueOf(6721975);

    @Bean
    public IEthereumNodeService getEthereumNodeService() {
        IEthereumNodeService nodeService = null;

        try {
            nodeService = new EthereumNodeService(rpcUrl, GANACHE_GAS_PRICE, GANACHE_GAS_LIMIT);
        } catch (ConnectException e) {
            e.printStackTrace();
        }

        return nodeService;
    }
}
